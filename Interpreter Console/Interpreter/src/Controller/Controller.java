package Controller;

import Exceptions.ADTException;
import Model.Program.ProgramState;
import Model.Value.IValue;
import Model.Value.ReferenceValue;
import Repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private IRepository repository;
    private ExecutorService executor;

    private boolean showSteps = false;
    private GarbageCollectorType garbageCollector = GarbageCollectorType.CONSERVATIVE;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    public IRepository getRepository() {
        return this.repository;
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    public List<ProgramState> getProgramList() {
        return this.repository.getProgramList();
    }

    public void setProgramList(List<ProgramState> programs) {
        this.repository.setProgramList(programs);
    }

    public void createExecutor() {
        this.executor = Executors.newFixedThreadPool(2);
    }

    public void killExecutor() {
        if (this.executor == null)
            return;

        this.executor.shutdownNow();
        this.executor = null;
    }

    public boolean getShowSteps() {
        return showSteps;
    }

    public void setShowSteps(boolean showSteps) {
        this.showSteps = showSteps;
    }

    public GarbageCollectorType getGC() {
        return garbageCollector;
    }

    public void setGC(GarbageCollectorType garbageCollector) {
        this.garbageCollector = garbageCollector;
    }

    private List<Integer> getAddressFromSymbolTable(Collection<IValue> symbolTableValues) {
        return symbolTableValues.stream()
                .filter(v -> v instanceof ReferenceValue)
                .map(v -> {
                    ReferenceValue v1 = (ReferenceValue)v;
                    return v1.getAddress();
                })
                .collect(Collectors.toList());
    }

    private List<Integer> getAddressFromHeap(Collection<IValue> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof ReferenceValue)
                .map(v-> {
                    ReferenceValue v1 = (ReferenceValue)v;
                    return v1.getAddress();
                })
                .collect(Collectors.toList());
    }

    private Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symbolTableAddress, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symbolTableAddress.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<Integer, IValue> safeGarbageCollector(List<Integer> symbolTableAddress, List<Integer> heapAddress, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> (symbolTableAddress.contains(e.getKey()) || heapAddress.contains(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void conservativeGarbageCollector(List<ProgramState> programStates) {
        List<Integer> symbolTableAddresses = Objects.requireNonNull(programStates.stream()
                        .map(p -> getAddressFromSymbolTable(p.getSymbolTable().values()))
                        .map(Collection::stream)
                        .reduce(Stream::concat).orElse(null))
                .collect(Collectors.toList());

        programStates.forEach(p -> p.getHeap().setHeap((HashMap<Integer, IValue>) safeGarbageCollector(symbolTableAddresses,
                getAddressFromHeap(p.getHeap().getHeap().values()), p.getHeap().getHeap())));
    }

    private void runGarbageCollector(List<ProgramState> programStates) throws InterruptedException {
        if (this.garbageCollector == GarbageCollectorType.UNSAFE) {
            // UNSAFE GC
            programStates.forEach(program -> program.getHeap().setHeap((HashMap<Integer, IValue>) unsafeGarbageCollector(
                    getAddressFromSymbolTable(program.getSymbolTable().getDictionary().values()),
                    program.getHeap().getHeap())));
        }
        else if (this.garbageCollector == GarbageCollectorType.SAFE) {
            // SAFE GC
            programStates.forEach(program -> program.getHeap().setHeap((HashMap<Integer, IValue>) safeGarbageCollector(
                    getAddressFromSymbolTable(program.getSymbolTable().getDictionary().values()),
                    getAddressFromHeap(program.getHeap().getHeap().values()),
                    program.getHeap().getHeap())));
        }
        else if (this.garbageCollector == GarbageCollectorType.CONSERVATIVE) {
            // CONSERVATIVE GC
            conservativeGarbageCollector(programStates);
        }
        else {
            // UNKNOWN GC
            throw new InterruptedException("Unknown garbage collector type!");
        }
    }

    public void oneStep(List<ProgramState> programStates) throws InterruptedException, ADTException, IOException {
        // Run the garbage collector
        this.runGarbageCollector(programStates);

        for (ProgramState programState : programStates) {
            if (programState.isOriginal()) {
                this.repository.logProgramStateExecution(programState);
            }
        }

        // RUN concurrently one step for each of the existing PrgStates
        // prepare the list of callables
        List<Callable<ProgramState>> callList = programStates.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());

        // start the execution of the callables
        // it returns the list of new created PrgStates (namely threads)
        List<Future<ProgramState>> futures = executor.invokeAll(callList);
        List<ProgramState> newProgramList = new ArrayList<>();

        for (Future<ProgramState> future : futures) {
            try {
                ProgramState programState = future.get();
                if (programState != null) {
                    newProgramList.add(programState);
                }
            } catch (ExecutionException e) {
                throw new ADTException(e.getCause().getMessage());
            }
        }

        // add the new created threads to the list of existing threads
        programStates.addAll(newProgramList);

        // after the execution, log the PrgState List
        for (ProgramState programState : programStates) {
            this.repository.logProgramStateExecution(programState);
        }

        // save the current programs in the repository
        this.setProgramList(programStates);
    }

    public void allSteps() throws InterruptedException, ADTException, IOException {
        this.createExecutor();

        // remove the completed programs
        List<ProgramState> programList = this.getUnfinishedPrograms();

        while (programList.size() > 0) {
            try {
                this.oneStep(programList);
            } catch (InterruptedException e) {
                throw new InterruptedException();
            }

            // remove the completed programs
            programList = this.getUnfinishedPrograms();
        }

        this.killExecutor();

        // HERE the repository still contains at least one Completed Prg
        // and its List<PrgState> is not empty. Note that oneStepForAllPrg calls the method
        // setPrgList of repository in order to change the repository
        // update the repository state
        this.setProgramList(programList);
    }

    public List<ProgramState> getUnfinishedPrograms() {
        return this.getProgramList()
                .stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }
}

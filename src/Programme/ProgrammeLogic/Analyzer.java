package Programme.ProgrammeLogic;

//Интерфейс анализаторов входных данных
interface Analyzer<T> {
    ProgrammeResult<T> analyze(T data);
}

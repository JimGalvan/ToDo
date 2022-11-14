package ToDo.data;

public final class ImportantTasksCount {

    private static ClassSingleton INSTANCE;
    private int count = 0;

    private static class ClassSingleton {
    }

    public ClassSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClassSingleton();
        }

        return INSTANCE;
    }

    public int getCount() {
        return this.count;
    }

    public int decrementCount() {
        return this.count--;
    }

    public int incrementCount() {
        return this.count++;
    }
}



// Создаем класс DataElement для хранения данных любого типа
class DataElement {
    // Объявляем поле для хранения данных
    private Object data;

    // Создаем конструктор с параметром data
    public DataElement(Object data) {
        this.data = data;
    }

    // Создаем геттер и сеттер для поля data
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // Переопределяем метод toString для отображения данных
    @Override
    public String toString() {
        return data.toString();
    }
}

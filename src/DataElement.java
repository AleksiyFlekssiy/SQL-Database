// ������� ����� DataElement ��� �������� ������ ������ ����
class DataElement {
    // ��������� ���� ��� �������� ������
    private Object data;

    // ������� ����������� � ���������� data
    public DataElement(Object data) {
        this.data = data;
    }

    // ������� ������ � ������ ��� ���� data
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // �������������� ����� toString ��� ����������� ������
    @Override
    public String toString() {
        return data.toString();
    }
}

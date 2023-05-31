/**
 * модель данных
 */
public class Model {
    private Long account;
    private String name;
    private Double value;

    public Model(long account, String name, double value) {
        this.account = account;
        this.name = name;
        this.value = value;
    }

    public Long getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Model{" +
                "account=" + account +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}

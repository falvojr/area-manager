package cleber.dias.areamanager.app.model.enuns;

public enum FormaPagamentoEnum {
	DINHEIRO(1), CHEQUE(2), DEPOSITO(3);

	private FormaPagamentoEnum(Integer id) {
		this.id = id;
	}

	private Integer id;

	public Integer getId() {
		return this.id;
	}
}

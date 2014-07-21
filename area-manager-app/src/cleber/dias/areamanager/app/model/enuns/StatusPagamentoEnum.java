package cleber.dias.areamanager.app.model.enuns;

public enum StatusPagamentoEnum {
	PAGO(1), NAO_PAGO(2);

	private StatusPagamentoEnum(int id) {
		this.id = id;
	}

	private int id;

	public int getId() {
		return this.id;
	}
}

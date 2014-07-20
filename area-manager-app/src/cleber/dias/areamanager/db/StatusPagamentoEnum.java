package cleber.dias.areamanager.db;

public enum StatusPagamentoEnum {
	PAGO(1, "Sim"),
	NAO_PAGO(2, "Não");

	private StatusPagamentoEnum(int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	private int id;
	private String descricao;

	public int getId() {
		return this.id;
	}

	public String getDescricao() {
		return this.descricao;
	}

}

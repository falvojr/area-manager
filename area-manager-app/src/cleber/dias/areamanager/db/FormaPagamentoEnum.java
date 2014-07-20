package cleber.dias.areamanager.db;

public enum FormaPagamentoEnum {
	DINHEIRO(1, "Dinheiro"),
	CHEQUE(2, "Cheque"),
	DEPOSITO(3, "Depósito Bancário");

	private FormaPagamentoEnum(int id, String descricao) {
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

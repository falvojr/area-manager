package cleber.dias.areamanager.db;

public enum FormaPagamentoEnum {
	DINHEIRO(1, "Dinheiro"),
	CHEQUE(2, "Cheque"),
	DEPOSITO(3, "Depósito");

	private FormaPagamentoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	private Integer id;
	private String descricao;

	public Integer getId() {
		return this.id;
	}

	public String getDescricao() {
		return this.descricao;
	}

}

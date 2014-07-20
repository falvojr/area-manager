package cleber.dias.areamanager.db;

import java.io.Serializable;

import android.annotation.SuppressLint;

import com.j256.ormlite.field.DatabaseField;

public class Reserva implements Serializable {

	private static final long serialVersionUID = 3686641463767314326L;

	@DatabaseField(generatedId = true)
	long id;
	@DatabaseField(canBeNull=false, index = true)
	long data;
	@DatabaseField(canBeNull=false)
	long cpf;
	@DatabaseField(canBeNull=false)
	String nome;
	@DatabaseField
	String endereco;
	@DatabaseField(canBeNull=false)
	String telefone;
	@DatabaseField
	Integer formaPagamento;
	@DatabaseField(canBeNull=false, index = true)
	int statusPagamento;

	public Reserva() {
		super();
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getData() {
		return this.data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public long getCpf() {
		return this.cpf;
	}

	public void setCpf(long cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return this.endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return this.telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Integer getFormaPagamento() {
		return this.formaPagamento;
	}

	public void setFormaPagamento(Integer formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public int getStatusPagamento() {
		return this.statusPagamento;
	}

	public void setStatusPagamento(int statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder()
		.append("id=").append(this.id).append(", ")
		.append("data=").append(this.data).append(", ")
		.append("cpf=").append(this.cpf).append(", ")
		.append("nome=").append(this.nome).append(", ")
		.append("endereco=").append(this.endereco).append(", ")
		.append("telefone=").append(this.telefone).append(", ")
		.append("formaPagamento=").append(this.formaPagamento).append(", ")
		.append("statusPagamento=").append(this.statusPagamento);
		return sb.toString();
	}
}

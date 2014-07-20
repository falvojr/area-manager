package cleber.dias.areamanager.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class Reserva {

	@DatabaseField(generatedId = true)
	long id;
	@DatabaseField(canBeNull=false)
	Date data;
	@DatabaseField(canBeNull=false, index = true)
	long cpf;
	@DatabaseField
	String nome;
	@DatabaseField
	String endereco;
	@DatabaseField(canBeNull=false)
	String telefone;
	@DatabaseField(dataType=DataType.INTEGER_OBJ)
	FormaPagamentoEnum formaPagamento;
	@DatabaseField(dataType=DataType.INTEGER_OBJ)
	StatusPagamentoEnum statusPagamento;

	public Reserva() {
		super();
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
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

	public FormaPagamentoEnum getFormaPagamento() {
		return this.formaPagamento;
	}

	public void setFormaPagamento(FormaPagamentoEnum formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public StatusPagamentoEnum getStatusPagamento() {
		return this.statusPagamento;
	}

	public void setStatusPagamento(StatusPagamentoEnum statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder()
		.append("id=").append(this.id).append(", ")
		.append("data=").append(new SimpleDateFormat("dd/MM/yyyy").format(this.data)).append(", ")
		.append("cpf=").append(this.cpf).append(", ")
		.append("nome=").append(this.nome).append(", ")
		.append("endereco=").append(this.endereco).append(", ")
		.append("telefone=").append(this.telefone).append(", ")
		.append("formaPagamento=").append(this.formaPagamento == null ? null : this.formaPagamento.getId()).append(", ")
		.append("statusPagamento=").append(this.statusPagamento == null ? null : this.statusPagamento.getId());
		return sb.toString();
	}
}

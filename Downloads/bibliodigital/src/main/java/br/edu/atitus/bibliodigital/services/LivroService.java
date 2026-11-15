package br.edu.atitus.bibliodigital.services;

import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.repositories.LivroRepository;
import java.util.List;

public class LivroService {

    private final LivroRepository repository = new LivroRepository();

    private String validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new RuntimeException("O campo " + campo + " não pode ficar vazio.");
        }
        return texto.trim();
    }

    private void validar(Livro livro, Livro livroAtual) {
        if (livro == null) {throw new RuntimeException("Livro inválido."); }

        livro.setTitulo(validarTexto(livro.getTitulo(), "título"));
        livro.setAutor(validarTexto(livro.getAutor(), "autor"));

        if (livro.getAnoPublicacao() < Livro.ANO_PUBLICACAO_MINIMO) { throw new RuntimeException
                ( "O ano de publicação deve ser igual ou maior que 1900."); }

        Livro encontrado = repository.findByTituloExato(livro.getTitulo());

        if (encontrado != null && encontrado != livroAtual) {throw new RuntimeException
                ("Já existe um livro cadastrado com esse título."); }

        switch (livro) {

            case LivroFisico fisico -> {
                fisico.setLocalizacao( validarTexto(fisico.getLocalizacao(), "localização"));

                if (fisico.getQuantidadeExemplares() <= 0) {throw new RuntimeException
                        ("A quantidade de exemplares deve ser maior que zero."); }
            }

            case LivroDigital digital -> {
                digital.setFormato(validarTexto(digital.getFormato(), "formato"));

                if (digital.getTamanhoArquivo() <= 0) {throw new RuntimeException
                        ("O tamanho do arquivo deve ser maior que zero."); }
            }

            case AudioLivro audio -> {
                audio.setNarrador(validarTexto(audio.getNarrador(), "narrador"));

                if (audio.getDuracao() <= 0) { throw new RuntimeException
                        ("A duração deve ser maior que zero."); }
            }
            default -> {
            }
        }
    }

    private void validarIndice(int indice) {
        List<Livro> livros = repository.findAll();

        if (livros.isEmpty()) {throw new RuntimeException
                ("O acervo está vazio."); }

        if (indice < 0 || indice >= livros.size()) {throw new RuntimeException
                ("Índice inválido. Escolha um livro da lista."); }
    }

    public void inserir(Livro livro) {validar(livro, null); repository.inserir(livro); }

    public List<Livro> findAll() { return repository.findAll(); }

    public List<Livro> findByTitulo(String parametro) {
        if (parametro == null || parametro.trim().isEmpty()) {throw new RuntimeException
                ("Informe um título ou parte dele para pesquisar."); }
        return repository.findByTitulo(parametro.trim());
    }

    public List<Livro> findByAutor(String parametro) {
        if (parametro == null || parametro.trim().isEmpty()) {throw new RuntimeException
                ("Informe um autor ou parte do nome para pesquisar."); }
        return repository.findByAutor(parametro.trim());
    }

    public Livro buscarPorIndice(int indice) {validarIndice(indice);
        return repository.buscarPorIndice(indice);
    }

    public void alterar(int indice, Livro livro) {validarIndice(indice);
        Livro livroAtual = repository.buscarPorIndice(indice);
        validar(livro, livroAtual);
        repository.alterar(indice, livro);
    }

    public void excluir(int indice) {
        validarIndice(indice);
        repository.excluir(indice);
    }
}
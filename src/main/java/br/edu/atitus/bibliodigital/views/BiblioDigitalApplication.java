package br.edu.atitus.bibliodigital.views;

import module java.base;
import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.services.LivroService;

public class BiblioDigitalApplication {
    private final LivroService service = new LivroService();

    void main() {
        int opcao = -1;

        do {
            mostrarMenu();
            String opcaoEntrada = IO.readln("Escolha uma das opções acima: ");

            try {
                // O trim() remove espaços antes e depois da opção digitada.
                opcao = Integer.parseInt(opcaoEntrada.trim());
            } catch (NumberFormatException ex) {
                IO.println("Digite uma opção numérica válida!");
                continue;
            }

            try {
                switch (opcao) {
                    case 1 -> cadastrarLivro();
                    case 2 -> pesquisarPorTitulo();
                    case 3 -> pesquisarPorAutor();
                    case 4 -> alterarLivro();
                    case 5 -> excluirLivro();
                    case 0 -> IO.println("Até mais!!");
                    default -> IO.println("Opção inválida.");
                }
            } catch (RuntimeException ex) {
                IO.println("Erro: " + ex.getMessage());
            }

            if (opcao != 0) {
                IO.readln("Pressione Enter para continuar...");
            }
        } while (opcao != 0);
    }

    private void mostrarMenu() {
        IO.println("""

                ===== Bem vindo(a) ao BiblioDigital =====

                Menu:

                1 - Cadastrar novo Livro

                2 - Pesquisar Livro por título

                3 - Pesquisar Livro por autor

                4 - Alterar dados do Livro

                5 - Excluir Livro

                0 - Sair
                """);
    }

    private void cadastrarLivro() {
        String menuTipo = """
                Qual tipo de livro deseja cadastrar?
                1 - Livro físico
                2 - Livro digital
                3 - Audiolivro

                Escolha uma opção:
                """;

        int opcaoTipo = lerInteiro(menuTipo);
        if (opcaoTipo < 1 || opcaoTipo > 3) {
            IO.println("Opção de tipo de livro inválida.");
            return;
        }

        Livro novoLivro = lerDadosLivro(opcaoTipo);
        service.inserir(novoLivro);
        IO.println("Livro cadastrado com sucesso!");
    }

    private Livro lerDadosLivro(int opcaoTipo) {
        String titulo = IO.readln("Digite o título do livro: ");
        String autor = IO.readln("Digite o autor do livro: ");
        int anoPublicacao = lerInteiro("Digite o ano de publicação do livro: ");

        if (opcaoTipo == 1) {
            int quantidadeExemplares = lerInteiro("Digite o número de exemplares: ");
            String localizacao = IO.readln("Digite a localização: ");
            return new LivroFisico(titulo, autor, anoPublicacao,
                    quantidadeExemplares, localizacao);
        } else if (opcaoTipo == 2) {
            String formato = IO.readln("Digite o formato do arquivo: ");
            double tamanhoArquivo = lerDouble("Digite o tamanho do arquivo: ");
            return new LivroDigital(titulo, autor, anoPublicacao,
                    formato, tamanhoArquivo);
        } else {
            int duracao = lerInteiro("Digite a duração do audiolivro: ");
            String narrador = IO.readln("Digite o nome do narrador: ");
            return new AudioLivro(titulo, autor, anoPublicacao, duracao, narrador);
        }
    }

    private void imprimirLivros(List<Livro> livros) {
        if (livros.isEmpty()) {
            IO.println("Não existe nenhum livro cadastrado.");
            return;
        }

        for (Livro livro : livros) {
            IO.println(livro);
            IO.println("-------------------------");
        }
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(IO.readln(mensagem).trim());
            } catch (NumberFormatException ex) {
                IO.println("Digite um número inteiro válido.");
            }
        }
    }

    private double lerDouble(String mensagem) {
        while (true) {
            try {
                return Double.parseDouble(IO.readln(mensagem).trim());
            } catch (NumberFormatException ex) {
                IO.println("Digite um número válido.");
            }
        }
    }

    private void pesquisarPorTitulo() {
        String pesquisaTitulo = IO.readln("Digite o título do livro ou parte dele: ");

        if (pesquisaTitulo == null || pesquisaTitulo.isBlank()) {
            IO.println("A pesquisa não pode estar vazia!");
            return;
        }

        List<Livro> livrosEncontrados = service.findByTitulo(pesquisaTitulo);
        if (livrosEncontrados.isEmpty()) {
            IO.println("Nenhum livro encontrado.");
        } else {
            imprimirLivros(livrosEncontrados);
        }
    }

    private void pesquisarPorAutor() {
        String pesquisaAutor = IO.readln("Digite o nome do autor ou parte dele: ");

        if (pesquisaAutor == null || pesquisaAutor.isBlank()) {
            IO.println("A pesquisa não pode estar vazia!");
            return;
        }

        List<Livro> livrosEncontrados = service.findByAutor(pesquisaAutor);
        if (livrosEncontrados.isEmpty()) {
            IO.println("Nenhum livro encontrado.");
        } else {
            imprimirLivros(livrosEncontrados);
        }
    }

    private void alterarLivro() {
        List<Livro> livros = service.findAll();
        if (livros.isEmpty()) {
            IO.println("Não existe nenhum livro cadastrado.");
            return;
        }

        IO.println("===== Livros cadastrados =====");
        for (int i = 0; i < livros.size(); i++) {
            IO.println((i + 1) + " - " + livros.get(i).getTitulo());
        }

        int indice = lerInteiro("Digite o número do livro que deseja alterar: ") - 1;
        Livro livroAtual = service.buscarPorIndice(indice);

        IO.println("\n===== Dados atuais do livro =====");
        IO.println(livroAtual);
        IO.println("-------------------------");
        IO.println("Digite os novos dados do livro:");

        int tipo = 1;
        if (livroAtual instanceof LivroDigital) {
            tipo = 2;
        } else if (livroAtual instanceof AudioLivro) {
            tipo = 3;
        }

        Livro livroNovo = lerDadosLivro(tipo);
        service.alterar(indice, livroNovo);
        IO.println("Livro alterado com sucesso!");
    }

    private void excluirLivro() {
        List<Livro> livros = service.findAll();
        if (livros.isEmpty()) {
            IO.println("Não existe nenhum livro cadastrado.");
            return;
        }

        IO.println("===== Livros cadastrados =====");
        for (int i = 0; i < livros.size(); i++) {
            IO.println((i + 1) + " - " + livros.get(i).getTitulo());
        }

        int indice = lerInteiro("Digite o número do livro que deseja excluir: ") - 1;
        Livro livro = service.buscarPorIndice(indice);
        String confirmacao = IO.readln(
                "Deseja excluir " + livro.getTitulo() + "? (S/N): ");

        if (confirmacao != null && confirmacao.trim().equalsIgnoreCase("S")) {
            service.excluir(indice);
            IO.println("Livro excluído com sucesso!");
        } else {
            IO.println("Exclusão cancelada.");
        }
    }
}

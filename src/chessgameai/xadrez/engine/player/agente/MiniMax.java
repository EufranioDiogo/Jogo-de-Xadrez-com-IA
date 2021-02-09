package chessgameai.xadrez.engine.player.agente;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.player.MoveTransition;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class MiniMax implements EstrategiaMovimento {
    private final AvaliadorTabuleiro avaliadorTabuleiro;
    private final int profundidade;
    File ficheiroQuantidadeTotal = new File("quantTotal.txt");
    File ficheiroQuantEntradas = new File("quantEntradas.txt");
    
    public MiniMax(final int profundidade) {
        this.avaliadorTabuleiro = new AvaliadorTabuleiroPadrao();
        this.profundidade = profundidade;
    }
    
    /*
    FileWriter caneta1 = new FileWriter(ficheiroQuantidadeTotal);
    FileWriter caneta2 = new FileWriter(ficheiroQuantEntradas);
    */
    @Override
    public Movimento executar(Tabuleiro tabuleiro) {
        Movimento melhorMovimento = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int valorActual;
        
        System.out.println("Jogador: " + tabuleiro.getJogadorActual().toString() + " Movimentos Possiveis: " + tabuleiro.getJogadorActual().getMovimentosLegais().size());
        
        for (final Movimento movimento : tabuleiro.getJogadorActual().getMovimentosLegais()) {
            System.out.println("*********************************************************");
            System.out.println("Origem: " + movimento.getPecaMovimentada() + 
                    TabuleiroUtils.getPosicaoParaCoordenada(movimento.getPecaMovimentada().getPosicaoPeca())
                    + "\nDestino: " + TabuleiroUtils.getPosicaoParaCoordenada(movimento.getCoordenadaDestino())
            );
            
            
            final MoveTransition movimentoTransicao = tabuleiro.getJogadorActual().fazerMovimento(movimento);

            if (movimentoTransicao.getEstadoMovimento().isDone()) {
                valorActual = tabuleiro.getJogadorActual().getJogadorAlliance() == Alliance.BLACK ? 
                        min(movimentoTransicao.getTabuleiro(), profundidade - 1, alpha, beta, 0) : 
                        max(movimentoTransicao.getTabuleiro(), profundidade - 1, alpha, beta, 0);

                System.out.println("Custo da decisão para o preto: " + valorActual);
                if (tabuleiro.getJogadorActual().getJogadorAlliance() == Alliance.WHITE && valorActual >= alpha) {
                    alpha = valorActual;
                    melhorMovimento = movimento;
                } else if (tabuleiro.getJogadorActual().getJogadorAlliance() == Alliance.BLACK && valorActual <= beta) {
                    beta = valorActual;
                    melhorMovimento = movimento;
                }
            }
        }
        return melhorMovimento;
    }

    public boolean isCenarioFimDoJogo(final Tabuleiro tabuleiro) {
        return tabuleiro.getJogadorActual().isEmCheckMate() || tabuleiro.getJogadorActual().isEmStaleMate();
    }
    
    public int min(final Tabuleiro tabuleiro, final int profundidade, int alpha, int beta, int quantNos) {
        if (profundidade == 0 || isCenarioFimDoJogo(tabuleiro)) {
            System.out.println("Valor do movimento: " + this.avaliadorTabuleiro.avaliar(tabuleiro, profundidade));
            System.out.println("--------------------------------------------------");
            return this.avaliadorTabuleiro.avaliar(tabuleiro, profundidade);
        }
        int valorMenorVisto = Integer.MAX_VALUE;
        
        System.out.println("Jogador: " + tabuleiro.getJogadorActual().toString() + " Movimentos Possiveis: " + tabuleiro.getJogadorActual().getMovimentosLegais().size());
        
        for (final Movimento movimento : tabuleiro.getJogadorActual().getMovimentosLegais()) {
            System.out.println("MIN*********************************************************");
            System.out.println("Origem: " + movimento.getPecaMovimentada() + 
                    TabuleiroUtils.getPosicaoParaCoordenada(movimento.getPecaMovimentada().getPosicaoPeca())
                    + "\nDestino: " + TabuleiroUtils.getPosicaoParaCoordenada(movimento.getCoordenadaDestino())
            );
            final MoveTransition movimentoTransicao = tabuleiro.getJogadorActual().fazerMovimento(movimento);
            
            if (movimentoTransicao.getEstadoMovimento().isDone()) {
                final int valorActual = max(movimentoTransicao.getTabuleiro(), profundidade - 1, alpha, beta, quantNos + 1);
                
                valorMenorVisto = Math.min(valorMenorVisto, valorActual);
                beta = Math.min(beta, valorMenorVisto);
                
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return valorMenorVisto;
    }
    
    public int max(final Tabuleiro tabuleiro, final int profundidade, int alpha, int beta, int quantNos) {
        if (profundidade == 0 || isCenarioFimDoJogo(tabuleiro)) {
            System.out.println("Valor do movimento: " + this.avaliadorTabuleiro.avaliar(tabuleiro, profundidade));
            return this.avaliadorTabuleiro.avaliar(tabuleiro, profundidade);
        }
        int valorMaiorVisto = Integer.MIN_VALUE;
        
        System.out.println("Jogador: " + tabuleiro.getJogadorActual().toString() + " Movimentos Possiveis: " + tabuleiro.getJogadorActual().getMovimentosLegais().size());
        
        for (final Movimento movimento : tabuleiro.getJogadorActual().getMovimentosLegais()) {
            System.out.println("MAX*********************************************************");
            System.out.println("Origem: " + movimento.getPecaMovimentada() + 
                    TabuleiroUtils.getPosicaoParaCoordenada(movimento.getPecaMovimentada().getPosicaoPeca())
                    + "\nDestino: " + TabuleiroUtils.getPosicaoParaCoordenada(movimento.getCoordenadaDestino())
            );
            final MoveTransition movimentoTransicao = tabuleiro.getJogadorActual().fazerMovimento(movimento);
            
            if (movimentoTransicao.getEstadoMovimento().isDone()) {
                final int valorActual = min(movimentoTransicao.getTabuleiro(), profundidade - 1, alpha, beta, quantNos + 1);
                
                valorMaiorVisto = Math.max(valorActual, valorMaiorVisto);
                alpha = Math.max(alpha, valorMaiorVisto);
                
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return valorMaiorVisto;
    }
    
    @Override
    public String toString() {
        return "MiniMax";
    }
}

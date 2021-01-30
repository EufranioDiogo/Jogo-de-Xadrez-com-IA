package chessgameai.xadrez.engine.player.agente;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.player.MoveTransition;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class MiniMax implements EstrategiaMovimento {
    private final AvaliadorTabuleiro avaliadorTabuleiro;
    private final int profundidade;
    
    public MiniMax(final int profundidade) {
        this.avaliadorTabuleiro = new AvaliadorTabuleiroPadrao();
        this.profundidade = profundidade;
    }
    
    @Override
    public Movimento executar(Tabuleiro tabuleiro) {
        Movimento melhorMovimento = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int valorActual;
        
        for (final Movimento movimento: tabuleiro.getJogadorActual().getMovimentosLegais()) {
            final MoveTransition movimentoTransicao = tabuleiro.getJogadorActual().fazerMovimento(movimento);
            
            if (movimentoTransicao.getEstadoMovimento().isDone()) {
                valorActual = tabuleiro.getJogadorActual().getJogadorAlliance() == Alliance.WHITE ? 
                        min(movimentoTransicao.getTabuleiro(), profundidade - 1, alpha, beta) : 
                        max(movimentoTransicao.getTabuleiro(), profundidade - 1, alpha, beta);
                
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
    
    public int min(final Tabuleiro tabuleiro, final int profundidade, int alpha, int beta) {
        if (profundidade == 0 || isCenarioFimDoJogo(tabuleiro)) {
            return this.avaliadorTabuleiro.avaliar(tabuleiro, profundidade);
        }
        int valorMenorVisto = Integer.MAX_VALUE;
        
        for (final Movimento movimento : tabuleiro.getJogadorActual().getMovimentosLegais()) {
            final MoveTransition movimentoTransicao = tabuleiro.getJogadorActual().fazerMovimento(movimento);
            
            if (movimentoTransicao.getEstadoMovimento().isDone()) {
                final int valorActual = max(movimentoTransicao.getTabuleiro(), profundidade - 1, alpha, beta);
                
                valorMenorVisto = Math.min(valorMenorVisto, valorActual);
                beta = Math.min(beta, valorMenorVisto);
                
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return valorMenorVisto;
    }
    
    public int max(final Tabuleiro tabuleiro, final int profundidade, int alpha, int beta) {
        if (profundidade == 0 || isCenarioFimDoJogo(tabuleiro)) {
            return this.avaliadorTabuleiro.avaliar(tabuleiro, profundidade);
        }
        int valorMaiorVisto = Integer.MIN_VALUE;
        
        for (final Movimento movimento : tabuleiro.getJogadorActual().getMovimentosLegais()) {
            final MoveTransition movimentoTransicao = tabuleiro.getJogadorActual().fazerMovimento(movimento);
            
            if (movimentoTransicao.getEstadoMovimento().isDone()) {
                final int valorActual = min(movimentoTransicao.getTabuleiro(), profundidade - 1, alpha, beta);
                
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

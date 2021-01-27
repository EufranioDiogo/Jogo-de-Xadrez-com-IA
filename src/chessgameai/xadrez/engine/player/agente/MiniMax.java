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
        //final long startTime = System.currentTimeMillis();
        
        Movimento melhorMovimento = null;
        int maiorValorVisto = Integer.MIN_VALUE;
        int menorValorVisto = Integer.MAX_VALUE;
        int valorActual;
        
        //int numMoves = tabuleiro.getJogadorActual().getMovimentosLegais().size();
        
        for (final Movimento movimento: tabuleiro.getJogadorActual().getMovimentosLegais()) {
            final MoveTransition movimentoTransicao = tabuleiro.getJogadorActual().fazerMovimento(movimento);
            
            if (movimentoTransicao.getEstadoMovimento().isDone()) {
                valorActual = tabuleiro.getJogadorActual().getJogadorAlliance() == Alliance.WHITE ? 
                        min(movimentoTransicao.getTabuleiro(), profundidade - 1) : 
                        max(movimentoTransicao.getTabuleiro(), profundidade - 1);
                
                if (tabuleiro.getJogadorActual().getJogadorAlliance() == Alliance.WHITE && valorActual >= maiorValorVisto) {
                    maiorValorVisto = valorActual;
                    melhorMovimento = movimento;
                } else if (tabuleiro.getJogadorActual().getJogadorAlliance() == Alliance.BLACK && valorActual <= menorValorVisto) {
                    menorValorVisto = valorActual;
                    melhorMovimento = movimento;
                }
                
            }
        }
        
        //final long tempoExecutado = System.currentTimeMillis() - startTime;
        
        //System.out.println("Tempo Executado: " + tempoExecutado);
        return melhorMovimento;
    }

    public boolean isCenarioFimDoJogo(final Tabuleiro tabuleiro) {
        return tabuleiro.getJogadorActual().isEmCheckMate() || tabuleiro.getJogadorActual().isEmStaleMate();
    }
    
    public int min(final Tabuleiro tabuleiro, final int profundidade) {
        if (profundidade == 0 || isCenarioFimDoJogo(tabuleiro)) {
            return this.avaliadorTabuleiro.avaliar(tabuleiro, profundidade);
        }
        int valorMenorVisto = Integer.MAX_VALUE;
        
        for (final Movimento movimento : tabuleiro.getJogadorActual().getMovimentosLegais()) {
            final MoveTransition movimentoTransicao = tabuleiro.getJogadorActual().fazerMovimento(movimento);
            
            if (movimentoTransicao.getEstadoMovimento().isDone()) {
                final int valorActual = max(movimentoTransicao.getTabuleiro(), profundidade - 1);
                
                if (valorActual <= valorMenorVisto) {
                    valorMenorVisto = valorActual;
                }
            }
        }
        return valorMenorVisto;
    }
    
    public int max(final Tabuleiro tabuleiro, final int profundidade) {
        if (profundidade == 0 || isCenarioFimDoJogo(tabuleiro)) {
            return this.avaliadorTabuleiro.avaliar(tabuleiro, profundidade);
        }
        int valorMaiorVisto = Integer.MIN_VALUE;
        
        for (final Movimento movimento : tabuleiro.getJogadorActual().getMovimentosLegais()) {
            final MoveTransition movimentoTransicao = tabuleiro.getJogadorActual().fazerMovimento(movimento);
            
            if (movimentoTransicao.getEstadoMovimento().isDone()) {
                final int valorActual = min(movimentoTransicao.getTabuleiro(), profundidade - 1);
                
                if (valorActual >= valorMaiorVisto) {
                    valorMaiorVisto = valorActual;
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

package chessgameai.xadrez.engine.pecas;
import chessgameai.Alliance;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Quadrado;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class Cavalo extends Peca{
    private final int[] POSSIVEIS_MOVIMENTO_VALIDOS = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Cavalo(final int posicaoPeca, final Alliance alliancePeca) {
        super(posicaoPeca, alliancePeca, TipoPeca.CAVALO, true);
    }
    
    public Cavalo(final int posicaoPeca, final Alliance alliancePeca, final boolean primeiroMovimento) {
        super(posicaoPeca, alliancePeca, TipoPeca.CAVALO, primeiroMovimento);
    }

    @Override
    public ArrayList<Movimento> calcularPossiveisMovimentos(Tabuleiro tabuleiro) {
        int coordenadaCandidataADestino;
        final ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        
        for(final int coordenada : POSSIVEIS_MOVIMENTO_VALIDOS) {
            coordenadaCandidataADestino = this.posicaoPeca + coordenada;
        
            
            if (TabuleiroUtils.isCoordenadaValida(coordenadaCandidataADestino)) {
                final Quadrado quadroCandidato = tabuleiro.getQuadrado(coordenadaCandidataADestino);
            
                if (isExculasaoPrimeiraColuna(posicaoPeca, coordenadaCandidataADestino) ||
                    isExculasaoSegundaColuna(posicaoPeca, coordenadaCandidataADestino) ||
                    isExculasaoSetimaColuna(posicaoPeca, coordenadaCandidataADestino) ||
                    isExculasaoOitavaColuna(posicaoPeca, coordenadaCandidataADestino)) {
                    continue;
                }
               
                if (!quadroCandidato.isQuadradoOcupado()) {
                    movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this,coordenadaCandidataADestino));
                } else {
                    if (this.alliancePeca != quadroCandidato.getPeca().getAlliancePeca()) {
                        movimentosPossiveis.add(new Movimento.MajorAttackMove(tabuleiro, this, coordenadaCandidataADestino));
                    }
                }
            }
        }
        return movimentosPossiveis;
    }
    
    private static boolean isExculasaoPrimeiraColuna(final int posicaoActual, final int possivelPosicao) {
        return TabuleiroUtils.PRIMEIRA_COLUNA[posicaoActual] && ((possivelPosicao == -17) || (possivelPosicao == -10) ||
                (possivelPosicao == 6) || (possivelPosicao == 15));
    }
    
    private static boolean isExculasaoSegundaColuna(final int posicaoActual, final int possivelPosicao) {
        return TabuleiroUtils.SEGUNDA_COLUNA[posicaoActual] && ((possivelPosicao == -10) || (possivelPosicao == -6));
    }
    
    private static boolean isExculasaoSetimaColuna(final int posicaoActual, final int possivelPosicao) {
        return TabuleiroUtils.SETIMA_COLUNA[posicaoActual] && ((possivelPosicao == -6) || (possivelPosicao == 10));
    }
    
    private static boolean isExculasaoOitavaColuna(final int posicaoActual, final int possivelPosicao) {
        return TabuleiroUtils.OITAVA_COLUNA[posicaoActual] && ((possivelPosicao == -17) || (possivelPosicao == -6) || (possivelPosicao == 10) ||
                (possivelPosicao == 17));
    }
    @Override
    public String toString() {
        return TipoPeca.CAVALO.toString();
    }
    @Override
    public Cavalo movimentarPeca(Movimento move) {
        return new Cavalo(move.getCoordenadaDestino(), move.getPecaMovimentada().getAlliancePeca());
    }
}

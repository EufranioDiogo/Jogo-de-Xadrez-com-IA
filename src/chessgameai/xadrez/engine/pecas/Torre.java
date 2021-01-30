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
public class Torre extends Peca {

    private static final int[] possiveisOffsetsDaTorre = {-8, 8, -1, 1};
    
    public Torre(int posicaoPeca, Alliance alliancePeca) {
        super(posicaoPeca, alliancePeca, TipoPeca.TORRE, true);
    }
    
    public Torre(int posicaoPeca, Alliance alliancePeca, final boolean primeiroMovimento) {
        super(posicaoPeca, alliancePeca, TipoPeca.TORRE, primeiroMovimento);
    }

    @Override
    public ArrayList<Movimento> calcularPossiveisMovimentos(Tabuleiro tabuleiro) {
        final ArrayList<Movimento> legalMoves = new ArrayList<>();
        
        if (TabuleiroUtils.isCoordenadaValida(this.posicaoPeca)) {
            for (final int currentCandidateOffset : possiveisOffsetsDaTorre) {
                int candidateDestinationCoordinate = this.posicaoPeca;
                while (TabuleiroUtils.isCoordenadaValida(candidateDestinationCoordinate)) {
                    if (isColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                        break;
                    }
                    candidateDestinationCoordinate += currentCandidateOffset;
                    if (TabuleiroUtils.isCoordenadaValida(candidateDestinationCoordinate)) {
                        final Peca pieceAtDestination = tabuleiro.getQuadrado(candidateDestinationCoordinate).getPeca();
                        if (pieceAtDestination == null) {
                            legalMoves.add(new Movimento.MovimentoSemAtaque(tabuleiro, this, candidateDestinationCoordinate));
                        } else {
                            final Alliance pieceAtDestinationAllegiance = pieceAtDestination.getAlliancePeca();
                            if (this.getAlliancePeca() != pieceAtDestinationAllegiance) {
                                legalMoves.add(new Movimento.MovimentoAtaque(tabuleiro, this, candidateDestinationCoordinate));
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        return legalMoves;
    }

    private boolean verificarOitavaColuna(int coordenada) {
        return TabuleiroUtils.OITAVA_COLUNA[coordenada];
    }
    
    private boolean verificarPrimeiraColuna(int coordenada) {
        return TabuleiroUtils.PRIMEIRA_COLUNA[coordenada];
    }
    @Override
    public String toString() {
        return TipoPeca.TORRE.toString();
    }
    @Override
    public Torre movimentarPeca(Movimento move) {
        return new Torre(move.getCoordenadaDestino(), move.getPecaMovimentada().getAlliancePeca());
    }
    
    private static boolean isColumnExclusion(final int currentCandidate,
                                             final int candidateDestinationCoordinate) {
        return (TabuleiroUtils.PRIMEIRA_COLUNA[candidateDestinationCoordinate] && (currentCandidate == -1)) ||
               (TabuleiroUtils.OITAVA_COLUNA[candidateDestinationCoordinate] && (currentCandidate == 1));
    }
}

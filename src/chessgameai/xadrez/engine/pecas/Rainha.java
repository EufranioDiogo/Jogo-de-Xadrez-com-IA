package chessgameai.xadrez.engine.pecas;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
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
public class Rainha extends Peca {
    private static final int[] possiveisOffsetsDaRainha = { -9, -8, -7, -1, 1,
        7, 8, 9 };
    public Rainha(int posicaoPeca, Alliance alliancePeca) {
        super(posicaoPeca, alliancePeca, TipoPeca.RAINHA, true);
    }
    public Rainha(int posicaoPeca, Alliance alliancePeca, final boolean primeiroMovimento) {
        super(posicaoPeca, alliancePeca, TipoPeca.RAINHA, primeiroMovimento);
    }

    @Override
    public ArrayList<Movimento> calcularPossiveisMovimentos(Tabuleiro tabuleiro) {
        final ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        
        for (final int currentCandidateOffset : possiveisOffsetsDaRainha) {
            int candidateDestinationCoordinate = this.posicaoPeca;
            while (true) {
                if (isFirstColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate) ||
                    isEightColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if (!TabuleiroUtils.isCoordenadaValida(candidateDestinationCoordinate)) {
                    break;
                } else {
                    final Peca pieceAtDestination = tabuleiro.getQuadrado(candidateDestinationCoordinate).getPeca();
                    if (pieceAtDestination == null) {
                        movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this, candidateDestinationCoordinate));
                    } else {
                        final Alliance pieceAtDestinationAllegiance = pieceAtDestination.getAlliancePeca();
                        if (this.alliancePeca != pieceAtDestinationAllegiance) {
                            movimentosPossiveis.add(new Movimento.MajorAttackMove(tabuleiro, this, candidateDestinationCoordinate));
                        }
                        break;
                    }
                }
            }
        }
        return movimentosPossiveis;
    }
    @Override
    public String toString() {
        return TipoPeca.RAINHA.toString();
    }
    @Override
    public Rainha movimentarPeca(Movimento move) {
        return new Rainha(move.getCoordenadaDestino(), move.getPecaMovimentada().getAlliancePeca());
    }
    
    private static boolean isFirstColumnExclusion(final int currentPosition,
                                                  final int candidatePosition) {
        return TabuleiroUtils.PRIMEIRA_COLUNA[candidatePosition] && ((currentPosition == -9)
                || (currentPosition == -1) || (currentPosition == 7));
    }

    private static boolean isEightColumnExclusion(final int currentPosition,
                                                  final int candidatePosition) {
        return TabuleiroUtils.OITAVA_COLUNA[candidatePosition] && ((currentPosition == -7)
                || (currentPosition == 1) || (currentPosition == 9));
    }
}

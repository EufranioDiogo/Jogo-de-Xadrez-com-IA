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
public class Rei extends Peca {
    private static final int[] possiveisOffsetsDoRei = {-9, -8, -7, -1, 1, 7, 8, 0};

    public Rei(final int posicaoPeca, final Alliance alliancePeca) {
        super(posicaoPeca, alliancePeca, TipoPeca.REI);
    }

    @Override
    public List<Movimento> calcularPossiveisMovimentos(Tabuleiro tabuleiro) {
        ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        
        for(final int possivelOffset : possiveisOffsetsDoRei) {
            int coordenadaCandidata = this.posicaoPeca + possivelOffset;
            
            if (isExculasaoOitavaColuna(posicaoPeca, possivelOffset) ||
                    isExculasaoPrimeiraColuna(posicaoPeca, possivelOffset)) {
                continue;
            }
            
            if (TabuleiroUtils.isCoordenadaValida(coordenadaCandidata)) {
                final Quadrado quadradoCandidato = tabuleiro.getQuadrado(coordenadaCandidata);
                
                if (!quadradoCandidato.isQuadradoOcupado()) {
                    movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this, coordenadaCandidata));
                } else {
                    movimentosPossiveis.add(new Movimento.MovimentoAtaque(tabuleiro, this, coordenadaCandidata));
                }
            }
        }
        
        return Collections.unmodifiableList(movimentosPossiveis);
    }

    private static boolean isExculasaoPrimeiraColuna(final int posicaoActual, final int possivelPosicao) {
        return TabuleiroUtils.PRIMEIRA_COLUNA[posicaoActual] && ((possivelPosicao == -9) || (possivelPosicao == -1) ||
                (possivelPosicao == 7));
    }
    
    private static boolean isExculasaoOitavaColuna(final int posicaoActual, final int possivelPosicao) {
        return TabuleiroUtils.OITAVA_COLUNA[posicaoActual] && ((possivelPosicao == -7) || (possivelPosicao == 1) ||
                (possivelPosicao == 7));
    }
    @Override
    public String toString() {
        return TipoPeca.REI.toString();
    }
    @Override
    public Rei movimentarPeca(Movimento move) {
        return new Rei(move.getCoordenadaDestino(), move.getPecaMovimentada().getAlliancePeca());
    }
}

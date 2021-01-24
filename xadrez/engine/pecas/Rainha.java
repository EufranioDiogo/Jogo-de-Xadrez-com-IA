package chessgameai.xadrez.engine.pecas;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class Rainha extends Peca {

    public Rainha(int posicaoPeca, Alliance alliancePeca) {
        super(posicaoPeca, alliancePeca, TipoPeca.RAINHA, true);
    }
    public Rainha(int posicaoPeca, Alliance alliancePeca, final boolean primeiroMovimento) {
        super(posicaoPeca, alliancePeca, TipoPeca.RAINHA, primeiroMovimento);
    }

    @Override
    public ArrayList<Movimento> calcularPossiveisMovimentos(Tabuleiro tabuleiro) {
        ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        
        ArrayList<Movimento> movimentosBispo = new Bispo(this.posicaoPeca, this.getAlliancePeca()).calcularPossiveisMovimentos(tabuleiro);
        ArrayList<Movimento> movimentosTorre = new Torre(this.posicaoPeca, this.getAlliancePeca()).calcularPossiveisMovimentos(tabuleiro);
        
        for (Movimento bispoMove : movimentosBispo) {
            movimentosPossiveis.add(bispoMove);
        }
        
        for (Movimento torreMovimento :  movimentosTorre) {
            if (!movimentosPossiveis.contains(torreMovimento)) {
                    movimentosPossiveis.add(torreMovimento);
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
}

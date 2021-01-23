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
        super(posicaoPeca, alliancePeca, TipoPeca.RAINHA);
    }

    @Override
    public List<Movimento> calcularPossiveisMovimentos(Tabuleiro tabuleiro) {
        ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        
        movimentosPossiveis.addAll(new Bispo(this.posicaoPeca, this.getAlliancePeca()).calcularPossiveisMovimentos(tabuleiro));
        movimentosPossiveis.addAll(new Torre(this.posicaoPeca, this.getAlliancePeca()).calcularPossiveisMovimentos(tabuleiro));
        
        return Collections.unmodifiableList(movimentosPossiveis);
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

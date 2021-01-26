package chessgameai.xadrez.engine.player.agente;

import chessgameai.xadrez.engine.pecas.Peca;
import chessgameai.xadrez.engine.player.Jogador;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;

/**
 * @Autor ed
 * Free Use - Livre_Uso
 */
public final class AvaliadorTabuleiroPadrao implements AvaliadorTabuleiro {

    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 50;
    private static final int BONUS_PROFUNDIDADE = 100;
     private static final int CASTLE_BONUS = 60;
    
    public AvaliadorTabuleiroPadrao() {
        
    }

    @Override
    public int avaliar(Tabuleiro tabuleiro, int profundidade) {
        return scoreJogador(tabuleiro, tabuleiro.getJogadorBranco(), profundidade) - 
               scoreJogador(tabuleiro, tabuleiro.getJogadorPreto(), profundidade);
        // Se scoreJogador > 0 bom movimento para Branco e scoreJogador < 0 bom movimento para Preto
    }

    private int scoreJogador(final Tabuleiro tabuleiro, final Jogador jogador, final int profundidade) {
       return valorPeca(jogador) + mobilidade(jogador) + check(jogador) + checkMate(jogador, profundidade) + 
               castled(jogador);
    }
    
    private int valorPeca(final Jogador jogador) {
        int valorTotalPecas = 0;
        
        for (final Peca peca : jogador.getPecasActivas()) {
            valorTotalPecas += peca.getValorPeca();
        }
        return valorTotalPecas;
    }

    private static int mobilidade(Jogador jogador) {
        return jogador.getMovimentosLegais().size();
    }
    
    private static int check(final Jogador jogador) {
        return jogador.getOponente().isEmCheck() ? CHECK_BONUS : 0;
    }

    private static int checkMate(final Jogador jogador, final int profundidade) {
        return jogador.getOponente().isEmCheckMate() ? CHECK_MATE_BONUS * bonusProfundidade(profundidade) : 0;
    }
    
    private static int bonusProfundidade(int profundidade) {
        return profundidade == 0 ? 1 : BONUS_PROFUNDIDADE * profundidade;
    }

    private static int castled(Jogador jogador) {
        return jogador.isCastled() ? CASTLE_BONUS : 0;
    }
}

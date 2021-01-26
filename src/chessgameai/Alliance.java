package chessgameai;

import chessgameai.xadrez.engine.player.Jogador;
import chessgameai.xadrez.engine.player.JogadorBranco;
import chessgameai.xadrez.engine.player.JogadorPreto;
import chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils;

public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public Jogador proximoJogador(final JogadorBranco jogadorBranco, final JogadorPreto jogadorPreto) {
            return jogadorBranco;
        }
        
        @Override
        public String toString() {
            return "W";
        }

        @Override
        public boolean isQuadradoPromocaoPinhao(int posicao) {
            return TabuleiroUtils.PRIMEIRA_LINHA[posicao];
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public Jogador proximoJogador(final JogadorBranco jogadorBranco, final JogadorPreto jogadorPreto) {
            return jogadorPreto;
        }
        @Override
        public String toString() {
            return "B";
        }

        @Override
        public boolean isQuadradoPromocaoPinhao(int posicao) {
            return TabuleiroUtils.OITAVA_LINHA[posicao];
        }
    };
    
    public abstract int getDirection();
    public abstract boolean isQuadradoPromocaoPinhao(int posicao);
    public abstract Jogador proximoJogador(JogadorBranco jogadorBranco, JogadorPreto jogadorPreto);
}

package chessgameai;

import chessgameai.xadrez.engine.player.Jogador;
import chessgameai.xadrez.engine.player.JogadorBranco;
import chessgameai.xadrez.engine.player.JogadorPreto;

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
    };
    
    public abstract int getDirection();
    public abstract Jogador proximoJogador(JogadorBranco jogadorBranco, JogadorPreto jogadorPreto);
}

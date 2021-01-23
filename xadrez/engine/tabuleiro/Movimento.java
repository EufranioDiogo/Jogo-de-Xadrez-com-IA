package chessgameai.xadrez.engine.tabuleiro;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.pecas.Peca;
import chessgameai.xadrez.engine.pecas.Pinhao;
import chessgameai.xadrez.engine.pecas.Torre;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro.Construtor;
import jdk.nashorn.internal.objects.annotations.Constructor;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public abstract class Movimento {
    final Tabuleiro tabuleiro;
    final Peca pecaASerMovimentada;
    final int coordenadaDestino;
    public static final Movimento NULL_MOVIMENTO = new NullMove();
    
    private Movimento(final Tabuleiro tabuleiro, final Peca pecaASerMovimentada, final int coordenadaDestino) {
        this.tabuleiro = tabuleiro;
        this.pecaASerMovimentada = pecaASerMovimentada;
        this.coordenadaDestino = coordenadaDestino;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        result = prime * result + this.coordenadaDestino;
        result = prime * result + this.pecaASerMovimentada.hashCode();
    
        return result;
    }
    
    @Override
    public boolean equals(final Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Movimento)) {
            return false;
        }
        final Movimento outroMovimento = (Movimento) outro;
        
        return getCoordenadaDestino() == outroMovimento.getCoordenadaDestino() &&
               getPecaMovimentada().equals(outroMovimento.getPecaMovimentada());
    }
    
    public int getCoordenadaDestino() {
        return this.coordenadaDestino;
    }
    
    public int getPosicaoActual() {
        return this.pecaASerMovimentada.getPosicaoPeca();
    }
    
    public Peca getPecaMovimentada() {
        return this.pecaASerMovimentada;
    }
    
    public boolean isAtaque() {
        return false;
    }
    
    public boolean isCastlingMove() {
        return false;
    }
    
    public Peca getPecaAtacada() {
        return null;
    }
    
    public Tabuleiro executarMovimento() {
       final Tabuleiro.Construtor constructor = new Construtor();

       for (final Peca peca : this.tabuleiro.getJogadorActual().getPecasActivas()) {
           if (this.pecaASerMovimentada.getPosicaoPeca() != peca.getPosicaoPeca()) {
               constructor.setPeca(peca);
           }
       }

       
       for (final Peca peca : this.tabuleiro.getJogadorActual().getOponente().getPecasActivas()) {
           constructor.setPeca(peca);
       }
        System.out.println("Saiu - Movimento");
       

       constructor.setPeca(this.getPecaMovimentada().movimentarPeca(this));
       
       constructor.setProximoJogadorAJogar(this.tabuleiro.getJogadorActual().getOponente().getJogadorAlliance());
       
       Tabuleiro tab = constructor.build();
       
        System.out.println("Lolsjbsj");
       return tab;
    }
    
    public static class MovimentoAtaque extends Movimento {
        final Peca pecaASerAtacada;
        public MovimentoAtaque(final Tabuleiro tabuleiro, final Peca pecaASerMovimentada, final int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
            pecaASerAtacada = tabuleiro.getQuadrado(coordenadaDestino).getPeca();
        }

        @Override
        public Tabuleiro executarMovimento() {
            return null;
        }
        
        @Override
        public boolean isAtaque() {
            return true;
        }
        
        @Override
        public Peca getPecaAtacada() {
            return this.pecaASerAtacada;
        }
        
        @Override
        public int hashCode() {
            return this.pecaASerAtacada.hashCode() + super.hashCode();
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof MovimentoAtaque)) {
                return false;
            }
            final MovimentoAtaque otherAtaque = (MovimentoAtaque) other;
            return super.equals(otherAtaque) && getPecaAtacada().equals(otherAtaque.getPecaAtacada());
        }
    }
    
    public static final class MovimentoSemAtaque extends Movimento {
        
        public MovimentoSemAtaque(final Tabuleiro tabuleiro, final Peca pecaASerMovimentada, final int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
    }
    
    public static class PinhaoMovimento extends Movimento {
        
        public PinhaoMovimento(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
        
    }
    
    public static class PinhaoMovimentoAtaque extends MovimentoAtaque {
        public PinhaoMovimentoAtaque(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
    }
    
    public static class PinhaoEnPassantMovimentoAtaque extends PinhaoMovimentoAtaque {
        public PinhaoEnPassantMovimentoAtaque(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
    }
    
    public static class PinhaoSalto extends Movimento {
        public PinhaoSalto(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
        
        @Override
        public Tabuleiro executarMovimento() {
            final Construtor constructor = new Construtor();


            for (final Peca peca : this.tabuleiro.getJogadorActual().getPecasActivas()) {
                if (!this.pecaASerMovimentada.equals(peca)) {
                    constructor.setPeca(peca);
                }
            }

            for (final Peca peca : this.tabuleiro.getJogadorActual().getOponente().getPecasActivas()) {
                constructor.setPeca(peca);
            }
            final Pinhao pinhaoMovimentado = (Pinhao) pecaASerMovimentada.movimentarPeca(this);
            

            constructor.setPeca(pinhaoMovimentado);
            constructor.setEnPassantPawn(pinhaoMovimentado);
            constructor.setProximoJogadorAJogar(this.tabuleiro.getJogadorActual().getOponente().getJogadorAlliance());

            return constructor.build();
        }
    }
    
    public abstract class CastleMove extends Movimento {
        protected final Torre castleTorre;
        protected final int castleTorrePosicaoInicio;
        protected final int castleTorrePosicaoFinal;
        
        public CastleMove(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino,
                final Torre castleTorre, final int castleTorrePosicaoInicio, final int castleTorrePosicaoFinal) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
            
            this.castleTorre = castleTorre;
            this.castleTorrePosicaoInicio = castleTorrePosicaoInicio;
            this.castleTorrePosicaoFinal = castleTorrePosicaoFinal;
        }
        
        public Torre getCastleTorre() {
            return this.castleTorre;
        }
        
        
        @Override
        public boolean isCastlingMove() {
            return true;
        }
        
        public Tabuleiro executarMovimento() {
            final Construtor constructor = new Construtor();


            for (final Peca peca : this.tabuleiro.getJogadorActual().getPecasActivas()) {
                if (!this.pecaASerMovimentada.equals(peca) && !peca.equals(this.castleTorre)) {
                    constructor.setPeca(peca);
                }
            }

            for (final Peca peca : this.tabuleiro.getJogadorActual().getOponente().getPecasActivas()) {
                constructor.setPeca(peca);
            }

            constructor.setPeca(this.getPecaMovimentada().movimentarPeca(this));
            constructor.setPeca(new Torre(this.castleTorrePosicaoFinal, this.castleTorre.getAlliancePeca()));
            constructor.setProximoJogadorAJogar(this.tabuleiro.getJogadorActual().getOponente().getJogadorAlliance());

            return constructor.build();
        }
    }
    
    public static final class ReiSideCastleMove extends CastleMove {
        public ReiSideCastleMove(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino,
                final Torre castleTorre, final int castleTorrePosicaoInicio, final int castleTorrePosicaoFinal) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino, castleTorre, castleTorrePosicaoInicio, castleTorrePosicaoFinal);
        }
        
        @Override
        public String toString() {
            return "0-0";
        }
    }
    
    public static final class RainhaSideCastleMove extends CastleMove {
        public RainhaSideCastleMove(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino,
                final Torre castleTorre, final int castleTorrePosicaoInicio, final int castleTorrePosicaoFinal) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino, castleTorre, castleTorrePosicaoInicio, castleTorrePosicaoFinal);
        }
        
        @Override
        public String toString() {
            return "0-0-0";
        }
    }
    
    public static class NullMove extends Movimento {
        public NullMove() {
            super(null, null, -1);
        }
        
        @Override
        public Tabuleiro executarMovimento() {
            throw new RuntimeException("Não é possível executar o movimento");
        }
    }
    
    public static class MovimentoFactory {
        
        private MovimentoFactory() {
            throw new RuntimeException("Not instantiable");
        }
        
        public static Movimento criarMovimento(final Tabuleiro tabuleiro, final int posicaoActual, final int posicaoDestino) {
            for(final Movimento movimento : tabuleiro.getMovimentosValidos()) {
                if (movimento.getPosicaoActual() == posicaoActual && movimento.getCoordenadaDestino() == posicaoDestino) {
                    System.out.println("encontrou movimento");
                    return movimento;
                }
            }
            return null;
        }
    }
    
}

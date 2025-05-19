import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

class Pokemones {
    String nombre;
    int ataque;
    int defensa;
    int hp;
    int maxHp;

    public Pokemones(String nombre, int ataque, int defensa, int hp, int maxHp) {
        this.nombre = nombre;
        this.ataque = ataque;
        this.defensa = defensa;
        this.hp = hp;
        this.maxHp = hp;
    }

    public int ataqueNormal(Pokemones oponente) {
        int danio = Math.max(ataque - oponente.defensa / 2, 5);
        oponente.hp -= danio;
        if (oponente.hp < 0) oponente.hp = 0;
        return danio;
    }

    public int ataqueEspecial(Pokemones oponente) {
        int danio = Math.max((ataque * 2) - oponente.defensa, 10);
        oponente.hp -= danio;
        if (oponente.hp < 0) oponente.hp = 0;
        return danio;
    }

    public int ataqueRapido(Pokemones oponente) {
        int danio = (int)(ataque * 0.7);
        oponente.hp -= danio;
        if (oponente.hp < 0) oponente.hp = 0;
        return danio;
    }

    public void curar() {
        hp += 20;
        if (hp > maxHp) hp=maxHp;
       
        
    }

    public boolean estaDerrotado() {
        return hp <= 0;
    }
}

public class Pokemon extends JFrame implements ActionListener {
    JTextArea areaTexto = new JTextArea(30, 60);
    JScrollPane scroll = new JScrollPane(areaTexto);
    JButton ataque1 = new JButton("Ataque");
    JButton ataque2 = new JButton("Ataque doble");
    JButton ataque3 = new JButton("Ataque rápido");
    JButton curar = new JButton("Curar");
    JComboBox<String> jugador1Selector = new JComboBox<>();
    JComboBox<String> jugador2Selector = new JComboBox<>();
    JButton iniciar = new JButton("¡Batalla!");

    HashMap<String, Pokemones> pokemones = new HashMap<>();
    Pokemones jugador1Pokemon, jugador2Pokemon;
    boolean turnoJugador1 = true;
    boolean batallaIniciada = false;

    public Pokemon() {
        setTitle("Batalla Pokémon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color fondoOscuro = new Color(30, 30, 30);
        Color textoClaro = new Color(230, 230, 230);
        Color botonOscuro = new Color(60, 60, 60);

        pokemones.put("Pikachu", new Pokemones("Pikachu", 40, 25, 150, 155));
        pokemones.put("Charmander", new Pokemones("Charmander", 35, 25, 145, 150));
        pokemones.put("Bulbasaur", new Pokemones("Bulbasaur", 30, 30, 160, 160));
        pokemones.put("Squirtle", new Pokemones("Squirtle", 30, 35, 148, 153));

        areaTexto.setBackground(fondoOscuro);
        areaTexto.setForeground(textoClaro);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaTexto.setEditable(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        JPanel seleccionPanel = new JPanel();
        seleccionPanel.setBackground(fondoOscuro);
        seleccionPanel.setLayout(new GridLayout(2, 3));

        JLabel label1 = new JLabel("Jugador 1", JLabel.CENTER);
        label1.setForeground(textoClaro);
        JLabel vs = new JLabel("vs", JLabel.CENTER);
        vs.setForeground(textoClaro);
        JLabel label2 = new JLabel("Jugador 2", JLabel.CENTER);
        label2.setForeground(textoClaro);

        seleccionPanel.add(label1);
        seleccionPanel.add(vs);
        seleccionPanel.add(label2);

        for (String nombre : pokemones.keySet()) {
            jugador1Selector.addItem(nombre);
            jugador2Selector.addItem(nombre);
        }

        seleccionPanel.add(jugador1Selector);
        seleccionPanel.add(iniciar);
        seleccionPanel.add(jugador2Selector);

        add(seleccionPanel, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(1, 4));
        panelBotones.setBackground(fondoOscuro);
        for (JButton boton : new JButton[]{ataque1, ataque2, ataque3, curar}) {
            boton.setBackground(botonOscuro);
            boton.setForeground(textoClaro);
            boton.setEnabled(false);
            panelBotones.add(boton);
            boton.addActionListener(this);
        }
        add(panelBotones, BorderLayout.SOUTH);

        iniciar.addActionListener(e -> iniciarBatalla());

        setSize(700, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void iniciarBatalla() {
        if (jugador1Selector.getSelectedItem().equals(jugador2Selector.getSelectedItem())) {
            areaTexto.append("❌ Los jugadores no pueden elegir el mismo Pokémon.\n");
            return;
        }

        jugador1Pokemon = clonarPokemon(pokemones.get((String) jugador1Selector.getSelectedItem()));
        jugador2Pokemon = clonarPokemon(pokemones.get((String) jugador2Selector.getSelectedItem()));
        batallaIniciada = true;
        turnoJugador1 = true;

        for (JButton boton : new JButton[]{ataque1, ataque2, ataque3, curar}) {
            boton.setEnabled(true);
        }

        areaTexto.setText("");
        areaTexto.append("🎮 ¡Bienvenidos a la batalla Pokémon por turnos!\n");
        areaTexto.append("🔵 Jugador 1 elegio a " + jugador1Pokemon.nombre + ".\n");
        areaTexto.append("🔴 Jugador 2 elegio a " + jugador2Pokemon.nombre + ".\n");
        areaTexto.append("\n🎯 Que empiece la batalla!! \n");
        areaTexto.append("Turno de jugador 1. \n\n");
    }

    private Pokemones clonarPokemon(Pokemones original) {
        return new Pokemones(original.nombre, original.ataque, original.defensa, original.hp, original.maxHp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!batallaIniciada) return;

        Pokemones atacante = turnoJugador1 ? jugador1Pokemon : jugador2Pokemon;
        Pokemones defensor = turnoJugador1 ? jugador2Pokemon : jugador1Pokemon;

        String nombreAtacante = turnoJugador1 ? "Jugador 1 (" + atacante.nombre + ")" : "Jugador 2 (" + atacante.nombre + ")";
        String nombreDefensor = turnoJugador1 ? "Jugador 2 (" + defensor.nombre + ")" : "Jugador 1 (" + defensor.nombre + ")";

        String resultado = "";

        if (e.getSource() == ataque1) {
            int danio = atacante.ataqueNormal(defensor);
            resultado += nombreAtacante + " atacó con un golpe directo y causó " + danio + " puntos de daño a " + defensor.nombre + ".\n";
        } else if (e.getSource() == ataque2) {
            int danio = atacante.ataqueEspecial(defensor);
            resultado += nombreAtacante + " lanzó un poderoso ataque especial, causando " + danio + " de daño a " + defensor.nombre + "!\n";
        } else if (e.getSource() == ataque3) {
            int danio = atacante.ataqueRapido(defensor);
            resultado += nombreAtacante + " realizó un ataque rápido. " + defensor.nombre + " recibió " + danio + " puntos de daño.\n";
        } else if (e.getSource() == curar) {
            int hpAntes = atacante.hp;
            atacante.curar();
            int curado = atacante.hp - hpAntes;
            resultado += nombreAtacante + " se tomó un respiro y recuperó " + curado + " puntos de salud. ¡Ahora tiene " + atacante.hp + " HP!\n";
        }

        if (defensor.estaDerrotado()) {
            resultado += "\n💥 " + nombreDefensor + " ya no puede continuar.\n";
            resultado += "🏆 ¡" + nombreAtacante + " es el ganador de esta épica batalla!\n";
            batallaIniciada = false;
            desactivarBotones();
        } else {
            resultado += "\n📊 Estado actual:\n";
            resultado += "🔵 " + jugador1Pokemon.nombre + ": " + jugador1Pokemon.hp + " HP\n";
            resultado += "🔴 " + jugador2Pokemon.nombre + ": " + jugador2Pokemon.hp + " HP\n";
            turnoJugador1 = !turnoJugador1;
            resultado += "\n🎯 Turno de " + (turnoJugador1 ? "Jugador 1" : "Jugador 2") + ". ¡Adelante!\n";
        }

        areaTexto.append(resultado);
        areaTexto.setCaretPosition(areaTexto.getDocument().getLength());
    }

    private void desactivarBotones() {
        ataque1.setEnabled(false);
        ataque2.setEnabled(false);
        ataque3.setEnabled(false);
        curar.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Pokemon::new);
    }
}

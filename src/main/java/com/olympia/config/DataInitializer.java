package com.olympia.config;

import com.olympia.entity.Musico;
import com.olympia.entity.Partitura;
import com.olympia.entity.Usuario;
import com.olympia.enums.Role;
import com.olympia.enums.TipoInstrumento;
import com.olympia.repository.MusicoRepository;
import com.olympia.repository.PartituraRepository;
import com.olympia.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final UsuarioRepository usuarioRepository;
    private final MusicoRepository musicoRepository;
    private final PartituraRepository partituraRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository u, MusicoRepository m, PartituraRepository p, PasswordEncoder pe) {
        this.usuarioRepository = u; this.musicoRepository = m;
        this.partituraRepository = p; this.passwordEncoder = pe;
    }

    @Override
    public void run(String... args) { criarAdmin(); criarMusicos(); criarPartituras(); }

    private void criarAdmin() {
        if (!usuarioRepository.existsByEmail("admin@olympia.com")) {
            usuarioRepository.save(Usuario.builder().nome("Administrador Olympia").email("admin@olympia.com")
                    .senha(passwordEncoder.encode("admin123")).role(Role.ROLE_ADMIN).ativo(true).build());
            log.info("✅ Admin criado: admin@olympia.com / admin123");
        }
        if (!usuarioRepository.existsByEmail("guilherme@olympia.com")) {
            usuarioRepository.save(Usuario.builder().nome("Guilherme Santana").email("guilherme@olympia.com")
                    .senha(passwordEncoder.encode("senha123")).role(Role.ROLE_USER).ativo(true).build());
            log.info("✅ Usuário criado: guilherme@olympia.com / senha123");
        }
    }

    private void criarMusicos() {
        if (musicoRepository.count() == 0) {
            musicoRepository.saveAll(List.of(
                Musico.builder().nome("Guilherme Santana").instrumento(TipoInstrumento.VIOLA).email("guilhermesantos_violista@hotmail.com").telefone("(11) 94298-3302").ativo(true).build(),
                Musico.builder().nome("Gabriel Geglio").instrumento(TipoInstrumento.VIOLINO).ativo(true).build(),
                Musico.builder().nome("Gilmar Santos").instrumento(TipoInstrumento.VIOLINO).ativo(true).build(),
                Musico.builder().nome("Diego Pereira").instrumento(TipoInstrumento.CELLO).telefone("(11) 95355-0550").ativo(true).build(),
                Musico.builder().nome("Rafael Akiyto").instrumento(TipoInstrumento.PIANO).ativo(true).build()
            ));
            log.info("✅ {} músicos cadastrados", musicoRepository.count());
        }
    }

    private void criarPartituras() {
        if (partituraRepository.count() == 0) {
            List<String[]> lista = List.of(
                new String[]{"93 Millions","Jason Mraz"},new String[]{"A Bela e a Fera","Howard Ashman & Alan Menken"},
                new String[]{"Agnus Dei","Michael W. Smith"},new String[]{"A Thousand Years","Christina Perri"},
                new String[]{"A Whole New World","Alan Menken"},new String[]{"Aleluia, Aleluia","Tradicional"},
                new String[]{"All of Me","John Legend"},new String[]{"Aquarela","Toquinho"},
                new String[]{"Aria","Johann Sebastian Bach"},new String[]{"Ave Maria","Franz Schubert"},
                new String[]{"Avengers Theme","Alan Silvestri"},new String[]{"Before You Go","Lewis Capaldi"},
                new String[]{"Can't Help Falling in Love","Elvis Presley"},new String[]{"Con te Partirò","Andrea Bocelli"},
                new String[]{"Can You Feel the Love Tonight","Elton John"},new String[]{"Canon in D","Johann Pachelbel"},
                new String[]{"Como É Grande Meu Amor por Você","Roberto Carlos"},new String[]{"De Janeiro a Janeiro","Roberta Campos"},
                new String[]{"Don't Want to Miss a Thing","Aerosmith"},new String[]{"Eine Kleine Nachtmusik","Mozart"},
                new String[]{"Eleanor Rigby","Beatles"},new String[]{"Escolhi Te Esperar","Marcela Tais"},
                new String[]{"Eu Sei Que Vou Te Amar","Tom Jobim"},new String[]{"Feeling Good","Michael Bublé"},
                new String[]{"Fly Me to the Moon","Frank Sinatra"},new String[]{"Game of Thrones Theme","Ramin Djawadi"},
                new String[]{"Georgia on My Mind","Hoagy Carmichael"},new String[]{"Hallelujah","Leonard Cohen"},
                new String[]{"Harry Potter Theme","John Williams"},new String[]{"Hey Jude","Beatles"},
                new String[]{"How Deep Is Your Love","Bee Gees"},new String[]{"I Won't Give Up","Jason Mraz"},
                new String[]{"I'm Yours","Jason Mraz"},new String[]{"If I Were a Boy","Beyoncé"},
                new String[]{"Inverno","Antonio Vivaldi"},new String[]{"Jesus Alegria dos Homens","Johann Sebastian Bach"},
                new String[]{"Just Give Me a Reason","Pink"},new String[]{"La Vie en Rose","Louis Armstrong"},
                new String[]{"Let It Be","Beatles"},new String[]{"Let It Go","Kristen Anderson-Lopez & Robert Lopez"},
                new String[]{"Libertango","Astor Piazzolla"},new String[]{"Love Me Like You Do","Ellie Goulding"},
                new String[]{"Love of My Life","Queen"},new String[]{"Lovely","Billie Eilish & Khalid"},
                new String[]{"Meu Abrigo","Melim"},new String[]{"Minha Felicidade","Roberta Campos"},
                new String[]{"My Way","Frank Sinatra"},new String[]{"New York, New York","Frank Sinatra"},
                new String[]{"Oceans","Hillsong United"},new String[]{"Oh Happy Day","Edwin Hawkins"},
                new String[]{"Perfect","Ed Sheeran"},new String[]{"Photograph","Ed Sheeran"},
                new String[]{"Por una Cabeza","Carlos Gardel"},new String[]{"Pra Você Guardei o Amor","Nando Reis"},
                new String[]{"Se Todos Fossem Iguais a Você","Gal Costa"},new String[]{"Secrets","OneRepublic"},
                new String[]{"Señorita","Camila Cabello & Shawn Mendes"},new String[]{"Shallow","Bradley Cooper & Lady Gaga"},
                new String[]{"Shape of You","Ed Sheeran"},new String[]{"Shout to the Lord","Hillsong United"},
                new String[]{"Someone You Loved","Lewis Capaldi"},new String[]{"Somewhere Over the Rainbow","Israel Kamakawiwoʻole"},
                new String[]{"Stand by Me","Ben E. King"},new String[]{"Stay with Me","Sam Smith"},
                new String[]{"Take on Me","A-ha"},new String[]{"The Scientist","Coldplay"},
                new String[]{"Thinking Out Loud","Ed Sheeran"},new String[]{"Titanium","David Guetta"},
                new String[]{"Todo Sentimento","Chico Buarque"},new String[]{"You've Got a Friend in Me","Randy Newman"},
                new String[]{"Trem Bala","Ana Vilela & Luan Santana"},new String[]{"Married Life (UP)","Michael Giacchino"},
                new String[]{"Viva la Vida","Coldplay"},new String[]{"Wake Me Up","Avicii"},
                new String[]{"Way Maker","Sinach"},new String[]{"What a Beautiful Name","Hillsong Worship"},
                new String[]{"What the World Needs Now Is Love","Jackie DeShannon"},
                new String[]{"You Are Not Alone","Michael Jackson"},new String[]{"You Raise Me Up","Josh Groban"}
            );
            lista.forEach(p -> partituraRepository.save(
                Partitura.builder().nomeMusica(p[0]).nomeCompositor(p[1]).disponivel(true).build()));
            log.info("✅ {} partituras cadastradas", partituraRepository.count());
        }
    }
}

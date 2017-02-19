import com.mongodb.MongoClient;
import com.omega.audio.AudioTrack;
import com.omega.audio.Playlist;
import com.omega.database.AudioTrackRepository;
import com.omega.database.PlaylistRepository;
import com.omega.database.morphium.MorphiumDatastoreManager;
import de.caluga.morphium.MorphiumConfig;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class MorphiumDBTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MorphiumDBTest.class);

    private static final int PORT = 12345;

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    private MongodExecutable mongodExe;
    private MongodProcess mongod;

    private MongoClient mongo;

    private MorphiumDatastoreManager datastoreManager;

    @Before
    public void setUp() throws IOException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        mongodExe = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net("localhost", PORT, Network.localhostIsIPv6()))
                .cmdOptions(new MongoCmdOptionsBuilder()
                        .syncDelay(10)
                        .useNoPrealloc(false)
                        .useSmallFiles(false)
                        .useNoJournal(false)
                        .enableTextSearch(true)
                        .build())
                .build());
        mongod = mongodExe.start();
        mongo = new MongoClient("localhost", PORT);

        MorphiumConfig cfg = new MorphiumConfig();
        cfg.setDatabase("discord_bot_test");
        cfg.addHostToSeed("localhost", PORT);
        datastoreManager = new MorphiumDatastoreManager(cfg);
    }

    @After
    public void cleanup() {
        if (this.mongod != null) {
            this.mongod.stop();
            this.mongodExe.stop();
        }
    }

    @Test
    public void canPersistAndLoadTracks() {
        AudioTrackRepository repository = datastoreManager.getRepository(AudioTrackRepository.class);

        AudioTrack track = new AudioTrack("Test title", "Author here", "source here", 1545);

        LOGGER.info("Persist track");
        repository.save(track);
        Object key = datastoreManager.getId(track);
        LOGGER.info("Generated ID : {}", key);

        LOGGER.info("Find persisted track");
        AudioTrack loadedTrack = repository.findById(key);

        assertNotNull("Unable to find persisted track", loadedTrack);
        assertEquals("Loaded track title doesn't match with persisted one", track.getTitle(), loadedTrack.getTitle());
    }

    @Test
    public void canPersistAndLoadPlaylist() {
        PlaylistRepository repository = datastoreManager.getRepository(PlaylistRepository.class);
        Playlist playlist = new Playlist("Playlist test", Playlist.Privacy.USER, "fdsf1sdfsdf0dsf", "rgfyf51f5zf4zq4qz");
        AudioTrack firstTrack = new AudioTrack("Test title", "Author here", "source here", 1545);
        AudioTrack secondTrack = new AudioTrack("Test title 2", "Author here", "source here", 45448);

        playlist.addTrack(firstTrack);
        playlist.addTrack(secondTrack);
        int playlistSize = playlist.getTracks().size();

        LOGGER.info("Persist playlist");
        repository.save(playlist);
        Object key = datastoreManager.getId(playlist);
        LOGGER.info("Generated ID : {}", key);

        LOGGER.info("Find persisted playlist");
        Playlist loadedPlaylist = repository.findById(key);
        int loadedPlaylistSize = loadedPlaylist.getTracks().size();

        assertNotNull("Unable to find persisted track", loadedPlaylist);
        assertEquals(
                String.format(
                        "Loaded playlist doesn't have the same number of tracks than the original one (%s / %s)",
                        loadedPlaylistSize, playlistSize
                ), loadedPlaylistSize, playlistSize);
    }
}

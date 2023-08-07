package com.freedomfm.singer;

import com.freedomfm.singer.repository.SingerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest({
        "spring.datasource.url=jdbc:mysql://localhost:3306/freedom_fm_test?useSSL=false",
        "spring.datasource.username=<your DB username>",
        "spring.datasource.password=<your DB password>"
})
@AutoConfigureMockMvc
public abstract class SingerApplicationTests {
    @Autowired
    protected SingerRepository singerRepository;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected MockMvc mvc;

    protected void cleanupTables() {
        String[] tables = {
                "verification_codes",
                "songs",
                "singers"
        };

        singerRepository.deleteAll();

        for (String table : tables) {
            String sql = "ALTER TABLE " + table + " AUTO_INCREMENT = 1";
            jdbcTemplate.execute(sql);
        }
    }
}

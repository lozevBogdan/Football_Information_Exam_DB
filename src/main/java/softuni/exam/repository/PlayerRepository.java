package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import softuni.exam.domain.entities.Player;

import java.math.BigDecimal;
import java.util.*;

public interface PlayerRepository extends JpaRepository<Player,Integer> {


    @Query("select p from Player p where p.team.name = :teamName")
    List<Player> findAllByTeamName(String teamName);

    List<Player> findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal salary);

}

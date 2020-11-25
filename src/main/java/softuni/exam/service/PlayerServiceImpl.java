package softuni.exam.service;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.PlayerImportDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Position;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final static String PATH_PLAYERS= "src/main/resources/files/json/players.json";


    private final PictureRepository pictureRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ValidatorUtil validatorUtil;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final Gson gson;

    public PlayerServiceImpl(PictureRepository pictureRepository, XmlParser xmlParser, ModelMapper modelMapper, FileUtil fileUtil, ValidatorUtil validatorUtil, PlayerRepository playerRepository, TeamRepository teamRepository, Gson gson) {
        this.pictureRepository = pictureRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.validatorUtil = validatorUtil;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.gson = gson;
    }


    @Override
    public String importPlayers() throws IOException {
     StringBuilder sb = new StringBuilder();

        PlayerImportDto[] playerImportDtos =
                this.gson.fromJson(this.readPlayersJsonFile(), PlayerImportDto[].class);

        for (PlayerImportDto playerImportDto : playerImportDtos) {

            try {
                Position position = Position.valueOf(playerImportDto.getPosition());

                Optional<Picture> byUrl =
                        this.pictureRepository.findByUrl(playerImportDto.getPicture().getUrl());

                Optional<Team> teamByName = this.teamRepository.findByName(playerImportDto.getTeam().getName());

                if(this.validatorUtil.isValid(playerImportDto) && byUrl.isPresent() &&
                        teamByName.isPresent()){

                    Player player = this.modelMapper.map(playerImportDto, Player.class);

                    player.setPicture(byUrl.get());
                    player.setTeam(teamByName.get());
                    player.setPosition(position);

                    this.playerRepository.saveAndFlush(player);

                    sb.append(String.format("Successfully imported player: %s %s%n",
                           playerImportDto.getFirstName() + " ",playerImportDto.getLastName()));

                }else {

                    sb.append("Invalid Player").append(System.lineSeparator());
                }


            }catch (Exception e){
                sb.append("Invalid Player").append(System.lineSeparator());
            }

        }


        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files.readString(Path
                .of(PATH_PLAYERS));
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder sb = new StringBuilder();

        List<Player> all =
                this.playerRepository.findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000));


        for (Player player : all) {

            sb.append(String.format("Player name: %s %s \n" +
                    "Number: %d\n" +
                    "Salary: %.2f\n" +
                    "Team: %s\n",player.getFirstName(),player.getLastName(),player.getNumber(),
                    player.getSalary(),player.getTeam().getName()));
        }

        return sb.toString();
    }

    @Override
    public String exportPlayersInATeam() {
StringBuilder sb = new StringBuilder();

String teamName = "North Hub";

        List<Player> north_hub =
                this.playerRepository.findAllByTeamName(teamName);

        sb.append(teamName).append(System.lineSeparator());

        for (Player player : north_hub) {
            sb.append(String.format(
                    "Player name: %s %s - %s\n" +
                    "Number: %d\n",
                   player.getFirstName(),player.getLastName(),player.getPosition(),
                    player.getNumber()));
        }

        return sb.toString();
    }
}

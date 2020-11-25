package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.TeamImportRootDto;
import softuni.exam.domain.dtos.TeamsImportDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private final String PATH_TEAMS =
            "src/main/resources/files/xml/teams.xml";

    private final TeamRepository teamRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ValidatorUtil validatorUtil;
    private final PictureRepository pictureRepository;

    public TeamServiceImpl(TeamRepository teamRepository, XmlParser xmlParser,
                           ModelMapper modelMapper, FileUtil fileUtil, ValidatorUtil validatorUtil, PictureRepository pictureRepository) {
        this.teamRepository = teamRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.validatorUtil = validatorUtil;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public String importTeams() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        TeamImportRootDto teamsImportDto = this.xmlParser
                .parseXml(TeamImportRootDto.class,
                PATH_TEAMS);

        for (TeamsImportDto importDto : teamsImportDto.getTeamsImportDtos()) {
            Optional<Picture> byUrl =
                    this.pictureRepository.findByUrl(importDto.getPicture().getUrl());

            if (this.validatorUtil.isValid(importDto) && byUrl.isPresent()){
                Team team = this.modelMapper.map(importDto, Team.class);
                team.setPicture(byUrl.get());
                this.teamRepository.saveAndFlush(team);
                sb.append(String.format
                        ("Successfully imported team- %s%n",team.getName()));
            }else {
                sb.append("Invalid team").append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {

        return this.fileUtil.readFile(PATH_TEAMS);
    }
}

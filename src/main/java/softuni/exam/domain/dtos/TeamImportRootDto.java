package softuni.exam.domain.dtos;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamImportRootDto {

    @XmlElement(name = "team")
    private List<TeamsImportDto> teamsImportDtos;


    public TeamImportRootDto() {
    }


    public List<TeamsImportDto> getTeamsImportDtos() {
        return teamsImportDtos;
    }

    public void setTeamsImportDtos(List<TeamsImportDto> teamsImportDtos) {
        this.teamsImportDtos = teamsImportDtos;
    }
}

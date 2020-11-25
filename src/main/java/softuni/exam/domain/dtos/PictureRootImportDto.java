package softuni.exam.domain.dtos;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pictures")
@XmlAccessorType(XmlAccessType.FIELD)
public class PictureRootImportDto {

    @XmlElement(name = "picture")
    private List<PictureImportDto> pictureImportDtos;

    public PictureRootImportDto() {
    }

    public List<PictureImportDto> getPictureImportDtos() {
        return pictureImportDtos;
    }

    public void setPictureImportDtos(List<PictureImportDto> pictureImportDtos) {
        this.pictureImportDtos = pictureImportDtos;
    }
}

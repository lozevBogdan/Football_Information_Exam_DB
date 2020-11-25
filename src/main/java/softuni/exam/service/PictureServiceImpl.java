package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softuni.exam.domain.dtos.PictureImportDto;
import softuni.exam.domain.dtos.PictureRootImportDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import softuni.exam.util.XmlParser;


import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, XmlParser xmlParser,
                              ModelMapper modelMapper, FileUtil fileUtil, ValidatorUtil validatorUtil) {
        this.pictureRepository = pictureRepository;
        this.xmlParser = xmlParser;

        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.validatorUtil = validatorUtil;

    }

    @Override
    public String importPictures() throws IOException, JAXBException {

        StringBuilder sb = new StringBuilder();

        PictureRootImportDto pictureRootImportDto =
                this.xmlParser.parseXml(PictureRootImportDto.class,
                        "src/main/resources/files/xml/pictures.xml");

        for (PictureImportDto pictureImportDto : pictureRootImportDto.getPictureImportDtos()) {

            if (this.validatorUtil.isValid(pictureImportDto)){
                Picture picture =
                        this.modelMapper.map(pictureImportDto, Picture.class);
                this.pictureRepository.saveAndFlush(picture);
                sb.append(String.format("Successfully imported picture- %s%n",picture.getUrl()));

            }else {
                sb.append("Invalid picture").append(System.lineSeparator());
            }

        }



        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {

        return this.fileUtil
                .readFile("src/main/resources/files/xml/pictures.xml");
    }

}

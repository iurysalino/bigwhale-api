package com.whale.web.design;

import com.whale.web.design.altercolor.model.AlterColorForm;
import com.whale.web.design.altercolor.service.AlterColorService;
import com.whale.web.design.colorspalette.model.PaletteForm;
import com.whale.web.design.colorspalette.model.ViewForm;
import com.whale.web.design.colorspalette.service.CreateColorsPaletteService;
import com.whale.web.utils.UploadImage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/design")
public class DesignController {


    private final AlterColorForm alterColorForm;

    private final AlterColorService alterColorService;

    private final UploadImage uploadImage;

    private final PaletteForm paletteForm;

    private final CreateColorsPaletteService createColorsPaletteService;

    public DesignController(AlterColorForm alterColorForm, AlterColorService alterColorService, UploadImage uploadImage,
                            PaletteForm paletteForm, CreateColorsPaletteService createColorsPaletteService) {
        this.alterColorForm = alterColorForm;
        this.alterColorService = alterColorService;
        this.uploadImage = uploadImage;
        this.paletteForm = paletteForm;
        this.createColorsPaletteService = createColorsPaletteService;
    }


    @GetMapping(value = "/altercolor")
    public String alterColor(Model model) {

        model.addAttribute("form", alterColorForm);
        return "altercolor";
    }


    @PostMapping("/altercolor")
    public String alterColor(AlterColorForm form, HttpServletResponse response) throws IOException {

        try {
            byte[] processedImage = alterColorService.alterColor(form.getImage(), form.getColorOfImage(), form.getColorForAlteration(), form.getMargin());

            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "attachment; filename=ModifiedImage.png");
            response.setHeader("Cache-Control", "no-cache");

            try (OutputStream os = response.getOutputStream()) {
                os.write(processedImage);
                os.flush();
            }
        } catch (Exception e) {
            response.sendRedirect("/design/altercolor");
        }

        return null;
    }

    @GetMapping(value = "/colorspalette")
    public String colorsPalette(Model model) {

        model.addAttribute("form", paletteForm);
        return "colorspalette";
    }

    @PostMapping("/colorspalette")
    public String colorsPalette(PaletteForm paletteForm, Model model) throws Exception {

        ViewForm viewForm = new ViewForm();

        MultipartFile upload = uploadImage.uploadImage(paletteForm.getImage());

        try {
            List<Color> listOfColors = createColorsPaletteService.createColorPalette(upload);

            viewForm.setListOfColors(listOfColors);
            viewForm.setImageBase64(Base64.getEncoder().encodeToString(paletteForm.getImage().getBytes()));

            model.addAttribute("form", viewForm);
            return "paletteview";

        } catch (Exception e) {
            return "redirect:/design/colorspalette";
        }
    }
}

package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.Menu;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "My Menus");

        return "menu/index";
    }

    @RequestMapping(value = "add")
    public String add(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("Add Menu");

        return "menu/add";


}
    @RequestMapping(value = "add", method=RequestMethod.POST)
    public String add(@ModelAttribute @Valid Menu newMenu,
                      Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);

        return "redirect:view/" + newMenu.getId();


}

    @RequestMapping(value = "view/{menuId}")
    public String viewMenu(Model model, @PathVariable int menuId) {

    Menu menu = menuDao.findOne(menuId);
    model.addAttribute("menuId", menu);


        return "menu/view";

}
    @RequestMapping(value = "add-item/{menuId}")
    public String addItem(Model model, @PathVariable int menuId) {

        Menu menu = menuDao.findOne(menuId);
        AddMenuItemForm form = new AddMenuItemForm(menu, cheeseDao.findAll());
        model.addAttribute("title", "Add Item To" + menu.getName());
        model.addAttribute("menuId", menu);

        return "menu/add-item";


    }
    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.POST)
    public String addItem(@ModelAttribute @PathVariable int menuId, @Valid AddMenuItemForm menuItemForm,
                          Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", menuItemForm.getMenu().getName());
            return "redirect:/add-item/" + menuId;
        }

        Menu menu = menuDao.findOne(menuItemForm.getMenuId());
        Cheese cheese = cheeseDao.findOne(menuItemForm.getCheeseId());
        menu.addItem(cheese);
        menuDao.save(menu);

        return "redirect:/menu/view" + menuId;


    }

}

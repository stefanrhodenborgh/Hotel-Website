package nl.srhodenborgh.royalfruitresorts.controller;

import nl.srhodenborgh.royalfruitresorts.model.Setting;
import nl.srhodenborgh.royalfruitresorts.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class SettingsController {
    @Autowired
    private SettingsService settingsService;



    // Create
    @PostMapping("/default-settings")
    public void loadDefaultSettings() {
        settingsService.loadDefaultSettings();
    }



    // Read
    @GetMapping("/all-settings")
    public Iterable<Setting> getAllSettings() {
        return settingsService.getAllSettings();
    }


    @GetMapping("/setting/{name}")
    public Optional<Setting> getSetting(@PathVariable ("name") String name) {
        return settingsService.getSetting(name);
    }



    // Update
    @PutMapping("/update-setting")
    public boolean updateSetting(@RequestBody Setting updatedSetting) {
        return settingsService.updateSetting(updatedSetting);
    }



    // Delete
    // Direct in database doen, niet via methode
}

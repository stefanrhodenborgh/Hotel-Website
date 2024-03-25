package nl.srhodenborgh.royalfruitresorts.service;

import nl.srhodenborgh.royalfruitresorts.model.Setting;
import nl.srhodenborgh.royalfruitresorts.repository.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;
    private static final Logger logger = LoggerFactory.getLogger(SettingsService.class);



    // Create
    public void loadDefaultSettings() {

        settingsRepository.save(new Setting("surchargeAmount", 25));
        settingsRepository.save(new Setting("loyaltyPointsStart", 0));
        settingsRepository.save(new Setting("loyaltyPointsAddition", 100));

        logger.info("Settings set to default");
    }



    //Read
    public Iterable<Setting> getAllSettings() {
        Iterable<Setting> settings = settingsRepository.findAll();

        if (!settings.iterator().hasNext()) {
            logger.error("No settings found in database");
        }

        return settings;
    }


    public Optional<Setting> getSetting(String name) {
        Optional<Setting> setting = settingsRepository.findById(name);

        if (setting.isEmpty()) {
            logger.error("Failed to get setting. Cannot find setting (name: {})", name);
        }

        return setting;
    }



    // Update
    public boolean updateSetting(Setting updatedSetting) {
        Optional<Setting> settingOptional = settingsRepository.findById(updatedSetting.getName());

        if (settingOptional.isEmpty()) {
            logger.error("Failed to update setting. Cannot find setting (name: {})", updatedSetting.getName());
            return false;
        }

        Setting setting = settingOptional.get();
        setting.setValue(updatedSetting.getValue());

        settingsRepository.save(setting);
        logger.info("Successfully updated setting (name: {}, value: {}", updatedSetting.getName(), updatedSetting.getValue());
        return true;
    }



    // Delete
    // Direct in database doen, niet via methode
}

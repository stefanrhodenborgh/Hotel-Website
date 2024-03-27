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

        settingsRepository.deleteAll();

        settingsRepository.save(new Setting("surchargeAmount", 25, "A fixed tax amount applied per stay when children are accommodated at the hotel"));
        settingsRepository.save(new Setting("loyaltyPointsStartAmount", 0, "The loyalty points start amount when an account is created"));
        settingsRepository.save(new Setting("loyaltyPointsAddition", 100, "The amount of loyalty points which account owners can earn per booking"));

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
        logger.info("Successfully updated setting (name: {}, value: {})", updatedSetting.getName(), updatedSetting.getValue());
        return true;
    }



    // Delete
    // Direct in database doen, niet via methode



    // Andere methodes
    public int getLoyaltyPointsStartAmount() {
        Optional<Setting> loyaltyPointsStartAmount = settingsRepository.findById("loyaltyPointsStartAmount");

        if (loyaltyPointsStartAmount.isEmpty()) {
            logger.warn("Cannot find loyaltyPointsStartAmount in settings. Amount is set to 0");
            return 0;
        }

        return loyaltyPointsStartAmount.get().getValue();
    }


    public int getLoyaltyPointsAddition() {
        Optional<Setting> loyaltyPointsAddition = settingsRepository.findById("loyaltyPointsAddition");

        if (loyaltyPointsAddition.isEmpty()) {
            logger.warn("Cannot find loyaltyPointsAddition in settings. Amount is set to 100");
            return 100;
        }

        return loyaltyPointsAddition.get().getValue();
    }


    public int getSurchargeAmount() {
        Optional<Setting> surchargeAmount = settingsRepository.findById("surchargeAmount");

        if (surchargeAmount.isEmpty()) {
            logger.warn("Cannot find surchargeAmount in settings. Amount is set to 100");
            return 25;
        }

        return surchargeAmount.get().getValue();
    }
}

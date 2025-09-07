package io.github.zhengzhengyiyi.tweak_api.api.configure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Generic JSON configuration manager for mod settings.
 * Handles loading, saving, and managing configuration options with change events.
 * 
 * <p><b>Usage example:</b>
 * <pre>{@code
 * JsonConfigManager<MyConfig> configManager = new JsonConfigManager<>("mymod", MyConfig.class);
 * MyConfig options = configManager.loadConfig();
 * 
 * // Register config change listener
 * configManager.registerConfigChangeListener((newConfig) -> {
 *     System.out.println("Config changed: " + newConfig);
 * });
 * 
 * // Modify and save config
 * configManager.saveConfig(options);
 * }</pre>
 * 
 * @param <T> the type of configuration class, must implement ConfigSchema
 * @author zhengzhengyiyi
 * @since 1.0.0
 */
public class JsonConfigManager<T extends JsonConfigManager.ConfigSchema> {
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create();
    
    public final String modId;
    private final Path configPath;
    private final Class<T> configClass;
    private final List<Consumer<T>> configChangeListeners = new ArrayList<>();
    
    /**
     * Marker interface for JSON-serializable configuration classes.
     * Only classes implementing this interface can be used with JsonConfigManager.
     */
    public interface ConfigSchema {
        /**
         * Called after configuration is loaded to validate and apply defaults.
         */
        void onConfigLoaded();
        
        /**
         * Called before configuration is saved to validate values.
         */
        void onConfigSaving();
    }
    
    /**
     * Configuration options inner class.
     * Implement ConfigSchema to make it JSON-serializable.
     */
    public static class ConfigOptions implements ConfigSchema {
        // Add your configuration fields here
        
        @Override
        public void onConfigLoaded() {
        }
        
        @Override
        public void onConfigSaving() {
        }
        
        @Override
        public String toString() {
            return "ConfigOptions{}";
        }
    }
    
    /**
     * Constructs a new JsonConfigManager for the specified mod and config class.
     * 
     * @param modId the mod identifier
     * @param configClass the configuration class type
     */
    public JsonConfigManager(String modId, Class<T> configClass) {
        this.modId = modId;
        this.configClass = configClass;
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve(modId + ".json");
    }
    
    /**
     * Loads configuration from file, or creates default config if not exists.
     * Automatically calls onConfigLoaded() after loading.
     * 
     * @return the loaded configuration options
     */
    public T loadConfig() {
        if (!Files.exists(configPath)) {
            T defaultConfig = createDefaultConfig();
            defaultConfig.onConfigLoaded();
            saveConfig(defaultConfig);
            return defaultConfig;
        }
        
        try {
            String json = Files.readString(configPath);
            T config = GSON.fromJson(json, configClass);  // 关键：使用 configClass 进行反序列化
            if (config != null) {
                config.onConfigLoaded();
                return config;
            } else {
                // 如果反序列化返回null，使用默认配置
                return createDefaultWithLoaded();
            }
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Failed to load config, using defaults: " + e.getMessage());
            return createDefaultWithLoaded();
        }
    }
    
    /**
     * Creates default configuration and calls onConfigLoaded.
     */
    private T createDefaultWithLoaded() {
        T defaultConfig = createDefaultConfig();
        defaultConfig.onConfigLoaded();
        return defaultConfig;
    }
    
    /**
     * Saves configuration to file and triggers change events.
     * Automatically calls onConfigSaving() before saving.
     * 
     * @param config the configuration options to save
     * @return true if saved successfully, false otherwise
     */
    public boolean saveConfig(T config) {
        config.onConfigSaving();
        
        try {
            if (!Files.exists(configPath.getParent())) {
                Files.createDirectories(configPath.getParent());
            }
            
            String json = GSON.toJson(config, configClass);
            Files.writeString(configPath, json);
            
            notifyConfigChangeListeners(config);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Registers a listener for configuration changes.
     * 
     * @param listener the listener to register
     */
    public void registerConfigChangeListener(Consumer<T> listener) {
        configChangeListeners.add(listener);
    }
    
    /**
     * Unregisters a configuration change listener.
     * 
     * @param listener the listener to unregister
     */
    public void unregisterConfigChangeListener(Consumer<T> listener) {
        configChangeListeners.remove(listener);
    }
    
    /**
     * Notifies all registered listeners about configuration changes.
     * 
     * @param newConfig the new configuration
     */
    private void notifyConfigChangeListeners(T newConfig) {
        for (Consumer<T> listener : configChangeListeners) {
            try {
                listener.accept(newConfig);
            } catch (Exception e) {
                System.err.println("Error in config change listener: " + e.getMessage());
            }
        }
    }
    
    /**
     * Creates default configuration options.
     * 
     * @return default configuration
     */
    public T createDefaultConfig() {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create default config instance for: " + configClass.getName(), e);
        }
    }
    
    /**
     * Gets the configuration file path.
     * 
     * @return the path to the config file
     */
    public Path getConfigPath() {
        return configPath;
    }
    
    /**
     * Reloads configuration from file and triggers change events.
     * 
     * @return the reloaded configuration options
     */
    public T reloadConfig() {
        T config = loadConfig();
        notifyConfigChangeListeners(config);
        return config;
    }
    
    /**
     * Resets configuration to defaults, saves, and triggers change events.
     * 
     * @return the default configuration options
     */
    public T resetToDefaults() {
        T defaultConfig = createDefaultConfig();
        defaultConfig.onConfigLoaded();
        saveConfig(defaultConfig);
        return defaultConfig;
    }
    
    /**
     * Checks if configuration file exists.
     * 
     * @return true if config file exists
     */
    public boolean configExists() {
        return Files.exists(configPath);
    }
    
    /**
     * Deletes the configuration file.
     * 
     * @return true if deleted successfully
     */
    public boolean deleteConfig() {
        try {
            return Files.deleteIfExists(configPath);
        } catch (IOException e) {
            System.err.println("Failed to delete config: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets the number of registered config change listeners.
     * 
     * @return listener count
     */
    public int getListenerCount() {
        return configChangeListeners.size();
    }
    
    /**
     * Clears all registered config change listeners.
     */
    public void clearAllListeners() {
        configChangeListeners.clear();
    }
    
    /**
     * Easy-to-use method for quick config access with default values.
     * 
     * @param defaultValue the default value to use if config doesn't exist
     * @return the configuration instance
     */
    public T getConfig(T defaultValue) {
        try {
            String json = Files.readString(configPath);
            T config = GSON.fromJson(json, configClass);
            if (config != null) {
                config.onConfigLoaded();
                return config;
            }
        } catch (Exception e) {
            // Use default value if config doesn't exist or is invalid
        }
        return defaultValue;
    }
    
    /**
     * Checks if the configuration file has been modified externally since last load.
     * Useful for detecting manual edits to the JSON file.
     * 
     * @return true if the file has been modified, false otherwise
     */
    public static boolean isConfigModifiedExternally(Path configPath, long lastKnownModifyTime) {
        try {
            if (!Files.exists(configPath)) {
                return false;
            }
            
            long currentModifyTime = Files.getLastModifiedTime(configPath).toMillis();
            return currentModifyTime > lastKnownModifyTime;
        } catch (IOException e) {
            System.err.println("Failed to check config modification time: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reloads configuration if it has been modified externally.
     * 
     * @return the reloaded configuration if modified, null otherwise
     */
    public T reloadIfModifiedExternally(long lastKnownModifyTime) {
        if (isConfigModifiedExternally(configPath, lastKnownModifyTime)) {
            System.out.println("Config file modified externally, reloading...");
            return reloadConfig();
        }
        return null;
    }

    /**
     * Gets the last modification time of the config file.
     * 
     * @return last modification time in milliseconds, 0 if file doesn't exist
     */
    public long getConfigLastModifiedTime() {
        try {
            if (Files.exists(configPath)) {
                return Files.getLastModifiedTime(configPath).toMillis();
            }
        } catch (IOException e) {
            System.err.println("Failed to get config modification time: " + e.getMessage());
        }
        return 0L;
    }
    
    /**
     * Static utility method to register config file modification checking to server tick events.
     * This will automatically reload configs when they are modified externally.
     * 
     * @param configManager the configuration manager instance to monitor
     * @param checkIntervalTicks the interval in ticks to check for modifications (20 ticks = 1 second)
     */
    public static void registerConfigModificationCheck(JsonConfigManager<?> configManager, int checkIntervalTicks) {
        final long[] lastModTime = {configManager.getConfigLastModifiedTime()};
        final int[] tickCounter = {0};
        
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter[0]++;
            
            if (tickCounter[0] >= checkIntervalTicks) {
                tickCounter[0] = 0;
                
                // Check if config was modified externally
                Object reloadedConfig = configManager.reloadIfModifiedExternally(lastModTime[0]);
                if (reloadedConfig != null) {
                    System.out.println("Config '" + configManager.modId + "' was modified externally and reloaded");
                    lastModTime[0] = configManager.getConfigLastModifiedTime();
                    
                    // You can add custom logic here for specific config types
                    if (reloadedConfig instanceof ConfigSchema) {
                        ((ConfigSchema) reloadedConfig).onConfigLoaded();
                    }
                }
            }
        });
    }

    /**
     * Static utility method to register config file modification check with default interval (1 second).
     * 
     * @param configManager the configuration manager instance to monitor
     */
    public static void registerConfigModificationCheck(JsonConfigManager<?> configManager) {
        registerConfigModificationCheck(configManager, 20);
    }

    /**
     * Static utility method to register multiple config managers for modification checking.
     * 
     * @param checkIntervalTicks the interval in ticks to check for modifications
     * @param configManagers the configuration manager instances to monitor
     */
    public static void registerMultipleConfigModificationCheck(int checkIntervalTicks, JsonConfigManager<?>... configManagers) {
        final long[] lastModTimes = new long[configManagers.length];
        for (int i = 0; i < configManagers.length; i++) {
            lastModTimes[i] = configManagers[i].getConfigLastModifiedTime();
        }
        
        final int[] tickCounter = {0};
        
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter[0]++;
            
            if (tickCounter[0] >= checkIntervalTicks) {
                tickCounter[0] = 0;
                
                for (int i = 0; i < configManagers.length; i++) {
                    JsonConfigManager<?> manager = configManagers[i];
                    Object reloadedConfig = manager.reloadIfModifiedExternally(lastModTimes[i]);
                    if (reloadedConfig != null) {
                        System.out.println("Config '" + manager.modId + "' was modified externally and reloaded");
                        lastModTimes[i] = manager.getConfigLastModifiedTime();
                        
                        if (reloadedConfig instanceof ConfigSchema) {
                            ((ConfigSchema) reloadedConfig).onConfigLoaded();
                        }
                    }
                }
            }
        });
    }
}

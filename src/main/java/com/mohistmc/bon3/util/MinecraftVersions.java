package com.mohistmc.bon3.util;

import com.mohistmc.bon3.data.BONFiles;
import com.mohistmc.bon3.util.MinecraftVersions.Manifest.Version;
import net.minecraftforge.srgutils.MinecraftVersion;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MinecraftVersions {
    private static final String MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    private static List<MinecraftVersion> knownVersions;
    private static final Map<MinecraftVersion, URL> versionUrls = new HashMap<>();

    public static List<MinecraftVersion> getKnownVersions() {
        return getKnownVersions(false);
    }

    public static List<MinecraftVersion> getKnownVersions(boolean force) {
        if (!force && knownVersions != null)
            return knownVersions;

        Set<MinecraftVersion> versions = new HashSet<>(findFromLauncherManifest());
        //TODO: Other discovery?
        knownVersions = versions.stream().sorted().toList();
        return knownVersions;
    }

    public static Map<String, URL> getDownloadUrls(MinecraftVersion version) {
        File versionF = new File(BONFiles.FG3_MC_CACHE, version + "/version.json");
        URL url = versionUrls.get(version);
        if (url == null) {
            System.out.println("Unknown version specific url for Minecraft Version: " + version);
            return Collections.emptyMap();
        }
        try {
            if (!DownloadUtils.downloadEtag(url, versionF, false)) {
                System.out.println("Failed to download version configuration from: " + url);
                return Collections.emptyMap();
            }
            VersionConfig cfg = IOUtils.loadJson(versionF, VersionConfig.class);
            if (cfg.downloads == null) {
                System.out.println("Failed to find mapping url, version config missing download section" + url);
                return Collections.emptyMap();
            }

            return cfg.downloads.entrySet().stream().filter(e -> e.getValue() != null && e.getValue().url != null).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().url));
        } catch (IOException e) {
            System.out.println("Failed to download version configuration from: " + url);
            e.printStackTrace();
            return null;
        }
    }

    private static Set<MinecraftVersion> findFromLauncherManifest() {
        File manifestF = new File(BONFiles.FG3_MC_CACHE, "manifest.json");
        try {
            if (!DownloadUtils.downloadEtag(new URL(MANIFEST_URL), manifestF, false)) {
                System.out.println("Failed to download Minecraft Version manifest from: " + MANIFEST_URL);
                return Collections.emptySet();
            }

            Manifest manifest = IOUtils.loadJson(manifestF, Manifest.class);
            if (manifest == null || manifest.versions == null)
                return Collections.emptySet();

            Set<MinecraftVersion> versions = new HashSet<>();
            for (Version v : manifest.versions) {
                if (!"release".equals(v.type))
                    continue; //TODO: Support selecting snapshots/finding the MCP version from that?
                MinecraftVersion ver = MinecraftVersion.from(v.id);
                versions.add(ver);
                if (v.url != null)
                    versionUrls.put(ver, v.url);
            }
            return versions;
        } catch (IOException e) {
            System.out.println("Failed to download Version manifest from: " + MANIFEST_URL);
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    public static class Manifest {
        public List<Version> versions;

        public static class Version {
            public String id;
            public String type;
            public URL url;
        }
    }

    public static class VersionConfig {
        public Map<String, Download> downloads;

        public static class Download {
            public URL url;
        }
    }
}

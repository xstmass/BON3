package com.mohistmc.bon3.srg;

import com.github.bsideup.jabel.Desugar;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Manifest;

@Desugar
public record ClassCollection(List<ClassNode> classes, Manifest manifest, Map<String, byte[]> extraFiles) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassCollection that = (ClassCollection) o;

        if (!Objects.equals(classes, that.classes)) {
            return false;
        }
        if (!Objects.equals(extraFiles, that.extraFiles)) {
            return false;
        }
        return Objects.equals(manifest, that.manifest);
    }

}

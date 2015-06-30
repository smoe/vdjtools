/*
 * Copyright 2013-2015 Mikhail Shugay (mikhail.shugay@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.antigenomics.vdjtools.profile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.milaboratory.core.sequence.AminoAcidSequence.ALPHABET;

public class AminoAcidProperty {
    public static AminoAcidProperty[] fromInput(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        do {
            line = reader.readLine();
        }
        while (line.startsWith("#"));

        String[] splitLine = line.split("\t");

        AminoAcidProperty[] groups = new AminoAcidProperty[splitLine.length - 1];

        for (int i = 0; i < groups.length; i++) {
            groups[i] = new AminoAcidProperty(splitLine[i + 1]);
        }

        while ((line = reader.readLine()) != null) {
            splitLine = line.split("\t");

            for (int i = 0; i < groups.length; i++) {
                groups[i].put(splitLine[0].charAt(0), Double.parseDouble(splitLine[i + 1]));
            }
        }

        return groups;
    }

    private final String name;
    private final double[] propertyByAA = new double[ALPHABET.size()];

    public AminoAcidProperty(String name) {
        this.name = name;
    }

    public void put(char aminoAcid, double value) {
        propertyByAA[ALPHABET.codeFromSymbol(aminoAcid)] = value;
    }

    public double getAt(char aminoAcid) {
        return getAt(ALPHABET.codeFromSymbol(aminoAcid));
    }

    public double getAt(byte code) {
        if (code < 0 || code >= propertyByAA.length) {
            throw new IndexOutOfBoundsException("Unknown amino acid code " + code);
        }

        return propertyByAA[code];
    }

    public String getName() {
        return name;
    }
}

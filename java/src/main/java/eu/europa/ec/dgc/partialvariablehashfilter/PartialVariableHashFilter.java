/*
 *  ---license-start
 *  eu-digital-green-certificates / dgca-partial-hash-filter
 *  ---
 *  Copyright (C) 2022 T-Systems International GmbH and all other contributors
 *  ---
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  ---license-end
 *
 *  Created by mykhailo.nester on 07/03/2022, 17:15
 */

package eu.europa.ec.dgc.partialvariablehashfilter;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

class PartialVariableHashFilter {

    private BigInteger[] array;
    private byte size;
    private int maxNumberOfElements;
    private int elementsCount;

    /**
     * Partial variable hash list filter initialization
     *
     * @param data bytearray of partial variable hashes
     */
    public PartialVariableHashFilter(byte[] data) {
        readFrom(data);
    }

    /**
     * Partial variable hash list filter initialization
     *
     * @param minSize          minimum size of the filter
     * @param partitionOffset  coordinate = 16, vector = 8, point = 0
     * @param numberOfElements elements in the filter
     * @param propRate         probability rate
     * @see PartitionOffset
     */
    public PartialVariableHashFilter(byte minSize, @NotNull PartitionOffset partitionOffset, int numberOfElements, float propRate) {
        byte actualSize = calc(partitionOffset.value, numberOfElements, propRate);

        this.maxNumberOfElements = numberOfElements;
        this.elementsCount = 0;
        this.array = new BigInteger[numberOfElements];

        if (actualSize < minSize) {
            size = minSize;
        } else {
            size = actualSize;
        }
    }

    private byte calc(byte partitionOffset, int numberOfElements, float propRate) {
        double num = Math.ceil(Math.log10(numberOfElements) / Math.log10(2));
        double rounded = num / 8 + num % 8;
        return (byte) Math.ceil(rounded - partitionOffset + propRate);
    }

    private void readFrom(byte @NotNull [] data) {
        if (data.length == 0) {
            return;
        }

        size = data[0];
        maxNumberOfElements = (data.length - 1) / size;
        array = new BigInteger[maxNumberOfElements];
        elementsCount = 0;

        int counter = 1;
        while (counter < data.length && elementsCount < maxNumberOfElements) {
            array[elementsCount] = new BigInteger(Arrays.copyOfRange(data, counter, counter + size));
            counter += size;
            elementsCount++;
        }

        Arrays.sort(array, Comparator.nullsLast(Comparator.naturalOrder()));
    }

    public byte[] writeTo() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(size);
        for (BigInteger bigInteger : array) {
            if(bigInteger != null) {
                byte[] bytes = bigInteger.toByteArray();
                int length = bytes.length;

                while (length < size) {
                    outputStream.write(0);
                    length++;
                }

                outputStream.write(bytes);
            }
        }

        return outputStream.toByteArray();
    }

    /**
     * Add hash data into searchable array of BigIntegers.
     *
     * @param data binary hash data
     * @throws IllegalArgumentException when data is less than partial hash size
     */
    public void add(byte @NotNull [] data) throws IllegalArgumentException {

        if (data.length < size) {
            throw new IllegalArgumentException("Data length cannot be less than partial hash size");
        }

        if (elementsCount == maxNumberOfElements) {
            Logger.getGlobal().warning("Filter has more elements than expected. " +
                "It may result in a higher False Positive Rate than defined!");

            maxNumberOfElements += 1;
            BigInteger [] newArray = new BigInteger[maxNumberOfElements];

            if (elementsCount >= 0) {
                System.arraycopy(array, 0, newArray, 0, elementsCount);
            }
            array = newArray;
        }

        array[elementsCount] = new BigInteger(Arrays.copyOf(data, size));
        elementsCount++;

        Arrays.sort(array, Comparator.nullsLast(Comparator.naturalOrder()));
    }

    /**
     * Check whether filter contains dcc hash bytes. It will check bytes depending on the filter size value.
     *
     * @param dccHashBytes byte array of dcc hash.
     * @return true is contains otherwise false
     */
    public boolean mightContain(byte[] dccHashBytes) {
        if (dccHashBytes.length < size) {
            return false;
        }

        byte[] filterSizeBytes = Arrays.copyOf(dccHashBytes, size);
        return new BinarySearch().binarySearch(array, 0, array.length, new BigInteger(filterSizeBytes));
    }

    public byte getSize() {
        return size;
    }

    public BigInteger[] getArray() {
        return array;
    }

    public int getElementsCount() {
        return elementsCount;
    }

}


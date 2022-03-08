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

package dgca.partialhash.filter;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

class PartialVariableHashFilter {

    private BigInteger[] array;
    private byte size;

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
    public PartialVariableHashFilter(byte minSize, PartitionOffset partitionOffset, int numberOfElements, float propRate) {
        byte actualSize = calc(partitionOffset.value, numberOfElements, propRate);

        if (actualSize < minSize) {
            size = minSize;
        } else {
            size = actualSize;
        }

        array = new BigInteger[size];
    }

    private byte calc(byte partitionOffset, int numberOfElements, float propRate) {
        double num = Math.log10(numberOfElements) / Math.log10(2);
        return (byte) (num - partitionOffset + propRate);
    }

    private void readFrom(byte @NotNull [] data) {
        if (data.length == 0) {
            return;
        }

        size = data[0];
        int numHashes = (data.length - 1) / size;
        array = new BigInteger[numHashes];

        int hashNumCounter = 0;
        int counter = 1;
        while (counter < data.length) {
            array[hashNumCounter] = new BigInteger(Arrays.copyOfRange(data, counter, counter + size));
            counter += size;
            hashNumCounter++;
        }
    }

    public byte[] writeTo() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(size);
        for (BigInteger bigInteger : array) {
            outputStream.write(bigInteger.toByteArray());
        }

        return outputStream.toByteArray();
    }

    public void add(byte @NotNull [] data) {
        if (data.length / size > array.length) {
            return;
        }

        int startPointer = data.length % size;

        int hashNumCounter = 0;
        while (startPointer < data.length) {
            array[hashNumCounter] = new BigInteger(Arrays.copyOfRange(data, startPointer, startPointer + size));
            startPointer += size;
            hashNumCounter++;
        }

        Arrays.sort(array);
    }

    public boolean mightContain(byte[] value) {
        return new BinarySearch().binarySearch(array, 0, array.length, new BigInteger(value));
    }

    public byte getSize() {
        return size;
    }

    public BigInteger[] getArray() {
        return array;
    }
}


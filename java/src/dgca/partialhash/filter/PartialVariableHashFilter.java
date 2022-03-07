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

//    short size; // (2 Bytes)
//    float proprate; // (4bytes)

    public PartialVariableHashFilter(byte[] data) {
        readFrom(data);
    }

//    public PartialVariableHashFilter(short minsize, byte partitionoffset, int numberOfElements, float proprate) {
//        proprate = prorate;
//        this.array = array;
//        actualsize = calc(partitionoffset, numberOfElements, proprate)
//
//        if (actualsize < minsize) size = minsize
//        else size = actualsize
//    }

    public void add(byte[] value) {
//        if (value.length > size) value.drop(...)  //drop the first bytes which are too much
//
//        array.append(BigInteger(value))
//        array.sort()  //bubble sort/quicksort
    }

    public boolean mightContain(byte[] value) {
        return new BinarySearch().binarySearch(array, 0, array.length, new BigInteger(value));
    }

    public void readFrom(byte @NotNull [] data) {
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
}


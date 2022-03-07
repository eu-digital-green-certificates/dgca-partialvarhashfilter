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
 *  Created by mykhailo.nester on 07/03/2022, 18:43
 */

package dgca.partialhash.filter;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class PartialVariableHashFilterTest {

    @Test
    public void mightContainTest() {
        byte[] byteArray = new byte[]{2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}; // length = 2
        byte[] searchedArray = new byte[]{3, 4};

        PartialVariableHashFilter filter = new PartialVariableHashFilter(byteArray);
        boolean result = filter.mightContain(searchedArray);
        assert result;
    }

    @Test
    public void filterToBinaryTest() throws IOException {
        byte[] byteArray = new byte[]{2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}; // length = 2

        PartialVariableHashFilter filter = new PartialVariableHashFilter(byteArray);
        byte[] result = filter.writeTo();
        assert Arrays.equals(result, byteArray);
    }
}

/*
 * Copyright 2014 Jose Lopes
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

/*
 * Copyright 2011-2014, by Vladimir Kostyukov and Contributors.
 * 
 * This file is part of la4j project (http://la4j.org)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributor(s): Wajdy Essam
 */
package net.fec.openrq.util.linearalgebra.matrix.dense;


import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import net.fec.openrq.util.linearalgebra.LinearAlgebra;
import net.fec.openrq.util.linearalgebra.matrix.ByteMatrices;
import net.fec.openrq.util.linearalgebra.matrix.ByteMatrix;
import net.fec.openrq.util.linearalgebra.matrix.source.MatrixSource;
import net.fec.openrq.util.linearalgebra.vector.ByteVector;
import net.fec.openrq.util.linearalgebra.vector.dense.BasicByteVector;


public class Basic1DByteMatrix extends AbstractBasicByteMatrix implements DenseByteMatrix {

    private static final long serialVersionUID = 4071505L;

    private byte self[];


    public Basic1DByteMatrix() {

        this(0, 0);
    }

    public Basic1DByteMatrix(ByteMatrix matrix) {

        this(ByteMatrices.asMatrixSource(matrix));
    }

    public Basic1DByteMatrix(MatrixSource source) {

        this(source.rows(), source.columns());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                self[i * columns + j] = source.get(i, j);
            }
        }
    }

    public Basic1DByteMatrix(byte array[][]) {

        this(array.length, array[0].length);

        int offset = 0;
        for (int i = 0; i < rows; i++) {
            System.arraycopy(array[i], 0, self, offset, columns);
            offset += columns;
        }
    }

    public Basic1DByteMatrix(int rows, int columns) {

        this(rows, columns, new byte[rows * columns]);
    }

    public Basic1DByteMatrix(int rows, int columns, byte array[]) {

        super(LinearAlgebra.BASIC1D_FACTORY, rows, columns);

        this.self = array;
    }

    @Override
    protected byte safeGet(int i, int j) {

        return self[i * columns + j];
    }

    @Override
    protected void safeSet(int i, int j, byte value) {

        self[i * columns + j] = value;
    }

    @Override
    public void swapRows(int i, int j) {

        checkRowBounds(i);
        checkRowBounds(j);
        if (i != j) {
            for (int k = 0; k < columns; k++) {
                byte tmp = self[i * columns + k];
                self[i * columns + k] = self[j * columns + k];
                self[j * columns + k] = tmp;
            }
        }
    }

    @Override
    public void swapColumns(int i, int j) {

        checkColumnBounds(i);
        checkColumnBounds(j);
        if (i != j) {
            for (int k = 0; k < rows; k++) {
                byte tmp = self[k * columns + i];
                self[k * columns + i] = self[k * columns + j];
                self[k * columns + j] = tmp;
            }
        }
    }

    @Override
    public ByteVector getRow(int i) {

        checkRowBounds(i);

        byte result[] = new byte[columns];
        System.arraycopy(self, i * columns, result, 0, columns);

        return new BasicByteVector(result);
    }

    @Override
    public ByteVector getRow(int i, int fromColumn, int toColumn) {

        checkRowBounds(i);
        checkColumnRangeBounds(fromColumn, toColumn);

        final int length = toColumn - fromColumn;
        byte[] result = new byte[length];
        System.arraycopy(self, (i * columns) + fromColumn, result, 0, length);

        return new BasicByteVector(result);
    }

    @Override
    public ByteMatrix copy() {

        byte $self[] = new byte[rows * columns];
        System.arraycopy(self, 0, $self, 0, rows * columns);
        return new Basic1DByteMatrix(rows, columns, $self);
    }

    @Override
    public ByteMatrix resize(int rows, int columns) {

        ensureDimensionsAreCorrect(rows, columns);

        if (this.rows == rows && this.columns == columns) {
            return copy();
        }

        if (this.rows < rows && this.columns == columns) {
            byte $self[] = new byte[rows * columns];
            System.arraycopy(self, 0, $self, 0, this.rows * columns);

            return new Basic1DByteMatrix(rows, columns, $self);
        }

        byte[] $self = new byte[rows * columns];

        int columnSize = columns < this.columns ? columns : this.columns;
        int rowSize = rows < this.rows ? rows : this.rows;

        for (int i = 0; i < rowSize; i++) {
            System.arraycopy(self, i * this.columns, $self, i * columns,
                columnSize);
        }

        return new Basic1DByteMatrix(rows, columns, $self);
    }

    @Override
    public byte[][] toArray() {

        byte result[][] = new byte[rows][columns];

        int offset = 0;
        for (int i = 0; i < rows; i++) {
            System.arraycopy(self, offset, result[i], 0, columns);
            offset += columns;
        }

        return result;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeInt(rows);
        out.writeInt(columns);

        for (int i = 0; i < rows * columns; i++) {
            out.writeByte(self[i]);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {

        rows = in.readInt();
        columns = in.readInt();

        self = new byte[rows * columns];

        for (int i = 0; i < rows * columns; i++) {
            self[i] = in.readByte();
        }
    }
}
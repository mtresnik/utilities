package com.resnik.util.math.symbo.logic;

public interface LogicalInterface<
        ANDPARAM extends LogicalInterface, ANDRET extends LogicalInterface,
        ORPARAM extends LogicalInterface, ORRET extends LogicalInterface,
        XORPARAM extends LogicalInterface, XORRET extends LogicalInterface,
        NOTRET extends LogicalInterface,
        IMPLIESPARAM extends LogicalInterface, IMPLIESRET extends LogicalInterface,
        IFFPARAM extends LogicalInterface, IFFRET extends LogicalInterface
        > {

    ANDRET and(ANDPARAM a1);

    ORRET or(ORPARAM a1);

    XORRET xor(XORPARAM a1);

    NOTRET not();

    IMPLIESRET implies(IMPLIESPARAM a1);

    IFFRET iff(IFFPARAM a1);
}

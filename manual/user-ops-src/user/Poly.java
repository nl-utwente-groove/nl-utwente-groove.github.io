package user;

import nl.utwente.groove.annotation.IntSeq;
import nl.utwente.groove.annotation.UserOperation;
import nl.utwente.groove.annotation.UserType;

/** A named polynomial, given by its coefficients in the order of increasing degree. */
@UserType
public record Poly(String name, IntSeq coeffs) {
    /** Returns this polynomial extended by a coefficient for the next higher degree. */
    @UserOperation
    public Poly append(int coeff) {
        return new Poly(name(), coeffs().append(coeff));
    }
}

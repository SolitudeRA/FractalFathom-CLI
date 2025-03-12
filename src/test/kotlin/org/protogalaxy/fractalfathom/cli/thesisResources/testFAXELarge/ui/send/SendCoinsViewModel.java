/*
 * Copyright the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.protogalaxy.fractalfathom.cli.thesisResources.testFAXELarge.ui.send;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import de.schildbach.wallet.WalletApplication;
import de.schildbach.wallet.data.*;
import de.schildbach.wallet.ui.AddressAndLabel;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.wallet.Wallet.BalanceType;

import javax.annotation.Nullable;
import java.util.List;

//&begin[SendCoins]
/**
 * @author Andreas Schildbach
 */
public class SendCoinsViewModel extends AndroidViewModel {
    public enum State {
        REQUEST_PAYMENT_REQUEST, //
        INPUT, // asks for confirmation
        DECRYPTING, SIGNING, SENDING, SENT, FAILED // sending states
    }

    private final WalletApplication application;
    public final WalletLiveData wallet;
    public final LiveData<List<AddressBookEntry>> addressBook;	//&line[AddressBook]
    public final SelectedExchangeRateLiveData exchangeRate;	//&line[ExchangeRates]
    public final DynamicFeeLiveData dynamicFees;	//&line[Fee]
    public final BlockchainStateLiveData blockchainState;
    public final WalletBalanceLiveData balance;	//&line[BitcoinBalance]

    @Nullable
    public State state = null;
    @Nullable
    public PaymentIntent paymentIntent = null;
    //&begin[Fee]
    public FeeCategory feeCategory = FeeCategory.NORMAL;	//&line[Normal]
    //&end[Fee]
    @Nullable
    public AddressAndLabel validatedAddress = null;
    @Nullable
    public Transaction sentTransaction = null;
    @Nullable
    public Boolean directPaymentAck = null;
    @Nullable
    public Transaction dryrunTransaction = null;
    @Nullable
    public Exception dryrunException = null;

    public SendCoinsViewModel(final Application application) {
        super(application);
        this.application = (WalletApplication) application;
        this.wallet = new WalletLiveData(this.application);
        this.addressBook = AppDatabase.getDatabase(this.application).addressBookDao().getAll();	//&line[AddressBook]
        this.exchangeRate = new SelectedExchangeRateLiveData(this.application);	//&line[ExchangeRates]
        this.dynamicFees = new DynamicFeeLiveData(this.application);	//&line[Fee]
        this.blockchainState = new BlockchainStateLiveData(this.application);
        this.balance = new WalletBalanceLiveData(this.application, BalanceType.AVAILABLE);	//&line[BitcoinBalance]
    }
}
//&end[SendCoins]